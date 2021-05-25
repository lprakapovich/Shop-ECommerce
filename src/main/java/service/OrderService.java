package service;

import com.mongodb.client.MongoCollection;
import exception.BadRequestException;
import exception.ResourceNotFoundException;
import model.order.Order;
import model.product.Product;
import org.bson.types.ObjectId;
import repository.OrderRepository;
import util.OrderQueryBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static api.Message.*;
import static model.order.Status.Cart;
import static model.order.Status.Processed;
import static util.Constants.AVAILABLE_QUANTITY;

public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final Map<Class<? extends Product>, ProductService> services;

    public OrderService(MongoCollection<Order> collection, UserService userService, Map<Class<? extends Product>, ProductService> services) {
        this.orderRepository = new OrderRepository(collection);
        this.userService = userService;
        this.services = services;
    }

    public String create(Order order, String authenticatedUser) {
        order.setStatus(Cart);
        order.setDate(LocalDate.now());
        order.setLastModifiedDate(LocalDate.now());
        validateOrderToCreate(order, authenticatedUser);
        return orderRepository.create(order);
    }

    public Order get(String id, String authenticatedUser) {
        return isAdmin(authenticatedUser)
                ? orderRepository.get(new ObjectId(id))
                : orderRepository.get(new ObjectId(id), authenticatedUser);
    }

    public List<Order> getAll(String authenticatedUser) {
        return isAdmin(authenticatedUser) ? orderRepository.getAll() : orderRepository.get(authenticatedUser);
    }

    public Order getCart(Map<String, List<String>> criteria) {
        return orderRepository.findOne(OrderQueryBuilder.buildQuery(criteria));
    }

    public List<Order> find(Map<String, List<String>> criteria) {
       return orderRepository.findMany(OrderQueryBuilder.buildQuery(criteria));
    }

    public Order update(Order order, String authenticatedUser) {
        validateOrderToUpdate(order, authenticatedUser);
        if (order.getStatus().equals(Processed)) {
            updateProductAvailability(order);
        }
        order.setLastModifiedDate(LocalDate.now());
        return orderRepository.update(order);
    }

    public void delete(String id, String authenticatedUser) {
        validateOrderToDelete(new ObjectId(id), authenticatedUser);
        orderRepository.delete(id);
    }

    private void validateOrderToCreate(Order order, String authenticatedUser) {
        validate(order, authenticatedUser);
    }

    private void validateOrderToUpdate(Order orderToUpdate, String authenticatedUser) {
        Order order = orderRepository.get(orderToUpdate.getId(), authenticatedUser);
        if (order == null) {
            throw new ResourceNotFoundException(ORDER_NOT_FOUND);
        }
        validate(orderToUpdate, authenticatedUser);
    }

    private void validateOrderToDelete(ObjectId id, String authenticatedUser) {
        Order order = orderRepository.get(id, authenticatedUser);
        if (order == null) {
            throw new ResourceNotFoundException(ORDER_NOT_FOUND);
        }
        checkIssuer(order, authenticatedUser);
        checkOrderDetails(order);
    }

    private void validate(Order order, String authenticatedUser) {
        checkIssuer(order, authenticatedUser);
        checkOrderDetails(order);
        checkOrderedItems(order);
    }

    // check if the product of actually available
    private void checkOrderedItems(Order order) {
        order.getOrderedItems().forEach(orderedItem -> {
            Product product = orderedItem.getProduct();
            Product fetchedProduct = services.get(product.getClass()).get(product.getId());
            if (fetchedProduct.getAvailableQuantity() == 0) {
                throw new BadRequestException(PRODUCT_NOT_AVAILABLE);
            } else if (fetchedProduct.getAvailableQuantity() < orderedItem.getOrderedQuantity()) {
                throw new BadRequestException(INVALID_ORDER_REQUESTED_QUANTITY_EXCEEDS_ACTUAL);
            } else {
                // set the most recent quantity value from the db for an easier update later on
                product.setAvailableQuantity(fetchedProduct.getAvailableQuantity());
            }
        });
    }

    private void checkOrderDetails(Order order) {
        if (missingProductsData(order)) {
            throw new BadRequestException(INVALID_ORDER);
        }
        order.getOrderedItems().forEach(orderedItem -> {
            Product product = orderedItem.getProduct();
            if (!services.get(product.getClass()).existsById(product.getId())) {
                throw new BadRequestException(INVALID_ORDER_NON_EXISTING_PRODUCTS);
            } else if (orderedItem.getOrderedQuantity() <= 0) {
                throw new BadRequestException(INVALID_ORDER_WRONG_PRODUCT_QUANTITY);
            }
        });
    }

    private void checkIssuer(Order order, String authenticatedUser) {
        if (!order.getIssuer().getEmail().equals(authenticatedUser)) {
            throw new BadRequestException(USERNAME_MISMATCH);
        }
    }

    private boolean missingProductsData(Order order) {
        return order.getOrderedItems().isEmpty();
    }

    private void updateProductAvailability(Order order) {
        order.getOrderedItems().forEach(orderedItem -> {
            Product orderedProduct = orderedItem.getProduct();
            int newQuantity = orderedProduct.getAvailableQuantity() - orderedItem.getOrderedQuantity();
            updateAvailableQuantityInProductCollection(orderedProduct, newQuantity);
            if (newQuantity == 0) {
                deleteUnavailableProductFromCart(orderedProduct.getId());
            } else {
                updateAvailableQuantityInOrderCollection(orderedProduct.getId(), newQuantity);
            }
        });
    }

    private void deleteUnavailableProductFromCart(ObjectId productId) {
        List<Order> orders = orderRepository.getByProductIdAndCart(productId);
        for (Order order : orders) {
            order.getOrderedItems().stream()
                    .filter(i -> i.getProduct().getId().equals(productId))
                    .findFirst()
                    .ifPresent(orderedItem -> order.getOrderedItems().remove(orderedItem));

            if (order.getOrderedItems().isEmpty()) {
                orderRepository.delete(order.getId());
            } else {
                orderRepository.update(order);
            }
        }
    }

    private void updateAvailableQuantityInOrderCollection(ObjectId productId, int newQuantity) {
        orderRepository.updateProductQuantityInCarts(productId, newQuantity);
        orderRepository.updateOrderedQuantityIfExceedsAvailable(productId, newQuantity);
    }

    private void updateAvailableQuantityInProductCollection(Product orderedProduct, int newQuantity) {
        services.get(orderedProduct.getClass()).updateField(orderedProduct.getId(), AVAILABLE_QUANTITY, newQuantity);
    }

    private boolean isAdmin(String authenticatedUser) {
        return userService.isAdmin(authenticatedUser);
    }
}

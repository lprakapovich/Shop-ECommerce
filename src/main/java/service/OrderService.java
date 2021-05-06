package service;

import com.mongodb.client.MongoCollection;
import exception.BadRequestException;
import exception.ResourceNotFoundException;
import model.order.Order;
import model.product.Product;
import org.bson.types.ObjectId;
import repository.OrderRepository;
import util.OrderQueryBuilder;

import java.util.List;
import java.util.Map;

import static api.Message.*;
import static model.order.OrderState.*;
import static util.Constants.QUANTITY;

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
        order.setOrderState(Cart);
        validateNewOrder(order, authenticatedUser);
        String orderId = orderRepository.create(order);
        userService.updateUserOrderList(authenticatedUser, orderId);
        return orderId;
    }

    public List<Order> get(Map<String, List<String>> criteria) {
        return orderRepository.find(OrderQueryBuilder.buildQuery(criteria));
    }

    public Order get(ObjectId id, String authenticatedUser) {
        return orderRepository.get(id, authenticatedUser)
                .orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND));
    }

    public Order update(Order order, String authenticatedUser) {
        validateExistingOrder(order.getId(), authenticatedUser);
        if (order.getOrderState().equals(Processed)) {
            updateProductsQuantities(order);
        }
        return orderRepository.update(order);
    }

    private void validateExistingOrder(ObjectId id, String authenticatedUser) {
        Order order = orderRepository.get(id, authenticatedUser)
                .orElseThrow(() -> new ResourceNotFoundException(""));
        validate(order, authenticatedUser);
    }

    private void validateNewOrder(Order order, String authenticatedUser) {
        validate(order, authenticatedUser);
    }

    private void validate(Order order, String authenticatedUser) {
        checkIssuer(order, authenticatedUser);
        checkOrderDetails(order);
        checkOrderedItems(order);
    }

    private void checkOrderedItems(Order order) {
        order.getOrderedItems().forEach(orderedItem -> {
            Product product = orderedItem.getProduct();
            Product fetchedProduct = services.get(product.getClass()).get(product.getId());
            if (fetchedProduct.getCurrentDbQuantity() == 0) {
                throw new BadRequestException(PRODUCT_NOT_AVAILABLE);
            } else if (fetchedProduct.getCurrentDbQuantity() < orderedItem.getOrderedQuantity()) {
                throw new BadRequestException(INVALID_ORDER_REQUESTED_QUANTITY_EXCEEDS_ACTUAL);
            } else {
                // set the most recent quantity value from the db for an easier update later on
                product.setCurrentDbQuantity(fetchedProduct.getCurrentDbQuantity());
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

    private void updateProductsQuantities(Order order) {
        order.getOrderedItems().forEach(orderedItem -> {
            Product orderedProduct = orderedItem.getProduct();
            int newQuantity = orderedProduct.getCurrentDbQuantity() - orderedItem.getOrderedQuantity();
            services.get(orderedProduct.getClass()).update(orderedProduct.getId(), QUANTITY, newQuantity);
        });
    }
}

package service;

import com.mongodb.client.MongoCollection;
import exception.BadRequestException;
import exception.ResourceNotFoundException;
import model.order.Order;
import repository.OrderRepository;
import util.OrderQueryBuilder;

import java.util.List;
import java.util.Map;

import static api.Message.*;
import static model.order.OrderState.Cart;

public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;

    public OrderService(MongoCollection<Order> collection, UserService userService) {
        this.orderRepository = new OrderRepository(collection);
        this.userService = userService;
    }

    public String create(Order order, String authenticatedUser) {
        check(order, authenticatedUser);
        order.setOrderState(Cart);
        String orderId = orderRepository.create(order);
        userService.updateUserOrderList(authenticatedUser, orderId);
        return orderId;
    }

    public List<Order> get(Map<String, List<String>> criteria) {
        return orderRepository.find(OrderQueryBuilder.buildQuery(criteria));
    }

    public Order get(String id, String authenticatedUser) {
        return orderRepository.get(id, authenticatedUser)
                .orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND));
    }

    public Order update(Order order, String authenticatedUser) {
        check(order, authenticatedUser);
        return orderRepository.update(order);
    }

    private void check(Order order, String authenticatedUser) {
        validateIssuer(order, authenticatedUser);
        validateOrder(order);
        validateProducts(order);
    }

    private void validateProducts(Order order) {
        order.getProducts().stream()
                .forEach(product -> {

                });
    }

    private void validateOrder(Order order) {
        if (missingProductsData(order)) {
            throw new BadRequestException(INVALID_ORDER);
        }
    }

    private void validateIssuer(Order order, String authenticatedUser) {
        if (!order.getIssuer().getEmail().equals(authenticatedUser)) {
            throw new BadRequestException(USERNAME_MISMATCH);
        }
    }

    private boolean missingProductsData(Order order) {
        return order.getProducts().isEmpty();
    }
}

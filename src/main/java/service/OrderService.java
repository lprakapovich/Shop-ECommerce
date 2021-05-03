package service;

import com.mongodb.client.MongoCollection;
import exception.BadRequestException;
import exception.ResourceNotFoundException;
import model.order.Order;
import repository.OrderRepository;
import util.OrderQueryBuilder;

import java.util.List;
import java.util.Map;

import static api.Message.ORDER_NOT_FOUND;
import static api.Message.USERNAME_MISMATCH;

public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;

    public OrderService(MongoCollection<Order> collection, UserService userService) {
        this.orderRepository = new OrderRepository(collection);
        this.userService = userService;
    }

    public String create(Order order, String authenticatedUser) {
        validateOrder(order, authenticatedUser);
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
        validateOrder(order, authenticatedUser);
        return orderRepository.update(order);
    }

    private void validateOrder(Order order, String authenticatedUser) {
        if (!order.getIssuer().getEmail().equals(authenticatedUser)) {
            throw new BadRequestException(USERNAME_MISMATCH);
        }
    }
}

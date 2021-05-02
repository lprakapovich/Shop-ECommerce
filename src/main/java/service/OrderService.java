package service;

import com.mongodb.client.MongoCollection;
import exception.BadRequestException;
import exception.ResourceNotFoundException;
import model.order.Order;
import repository.OrderRepository;

import java.util.List;

import static api.Message.*;

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

    public List<Order> get(List<String> ids, String authenticatedUser) {
        List<Order> orders = orderRepository.get(ids, authenticatedUser);
        validateList(orders);
        return orders;
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

    private void validateList(List<?> items) {
        if (items.isEmpty()) {
            throw new ResourceNotFoundException(ITEM_NOT_FOUND);
        }
    }
}

package service;

import com.mongodb.client.MongoCollection;
import exception.BadRequestException;
import model.order.Order;
import repository.OrderRepository;

import static api.Message.USERNAME_MISMATCH;

public class OrderService {

    protected final OrderRepository orderRepository;

    public OrderService(MongoCollection<Order> collection) {
        this.orderRepository = new OrderRepository(collection);
    }

    public String create(Order order, String user) {
        validateOrder(order, user);
        return orderRepository.create(order);
    }

    private void validateOrder(Order order, String authenticatedUserEmail) {
        if (!order.getIssuer().getEmail().equals(authenticatedUserEmail)) {
            throw new BadRequestException(USERNAME_MISMATCH);
        }
    }
}

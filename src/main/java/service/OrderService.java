package service;

import model.order.Order;
import repository.OrderRepository;

public class OrderService {

    protected final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public String create(Order order) {

        return null;
    }
}

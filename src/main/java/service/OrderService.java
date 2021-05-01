package service;

import com.mongodb.client.MongoCollection;
import model.order.Order;
import repository.OrderRepository;

public class OrderService {

    protected final OrderRepository orderRepository;

    public OrderService(MongoCollection<Order> collection) {
        this.orderRepository = new OrderRepository(collection);
    }

    public String create(Order order) {

        return null;
    }
}

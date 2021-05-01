package repository;

import com.mongodb.client.MongoCollection;
import model.order.Order;

public class OrderRepository extends MongoRepository<Order> {

    public OrderRepository(MongoCollection<Order> collection) {
        super(collection);
    }
}

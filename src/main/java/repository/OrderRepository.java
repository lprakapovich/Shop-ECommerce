package repository;

import com.mongodb.client.MongoCollection;
import model.order.Order;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderRepository extends MongoRepository<Order> {

    public OrderRepository(MongoCollection<Order> collection) {
        super(collection);
    }

    public Optional<Order> get(String id, String issuer) {
        return super.get(id)
                .stream()
                .filter(order -> order.getIssuer().getEmail().equals(issuer))
                .findFirst();
    }

    public List<Order> get(List<String> ids, String issuer) {
        return super.get(ids)
                .stream()
                .filter(order -> order.getIssuer().getEmail().equals(issuer))
                .collect(Collectors.toList());
    }
}

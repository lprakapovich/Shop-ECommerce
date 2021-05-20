package repository;

import com.mongodb.client.MongoCollection;
import model.order.Order;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static util.Constants.DATABASE_ID;

public class OrderRepository extends MongoRepository<Order> {

    public OrderRepository(MongoCollection<Order> collection) {
        super(collection);
    }

    public Order get(ObjectId id, String issuer) {
        return collection.find(and(eq(DATABASE_ID, id), eq("issuer.email", issuer))).first();
    }

    public List<Order> get(String issuer) {
        return collection.find(eq("issuer.email", issuer)).into(new ArrayList<>());
    }
}

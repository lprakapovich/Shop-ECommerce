package repository;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import model.user.User;

import static com.mongodb.client.model.Filters.eq;
import static util.Constants.EMAIL;
import static util.Constants.ORDER_IDS;

public class UserRepository extends MongoRepository<User> {

    public UserRepository(MongoCollection<User> collection) {
        super(collection);
    }

    public void updateOrderList(String userEmail, String orderId) {
        BasicDBObject list = new BasicDBObject(ORDER_IDS, orderId);
        BasicDBObject update = new BasicDBObject("$push", list);
        collection.updateOne(eq(EMAIL, userEmail), update);
    }
}

package repository;

import com.mongodb.client.MongoCollection;
import model.user.User;

import static com.mongodb.client.model.Filters.eq;

public class UserRepository extends MongoRepository<User> {

    public UserRepository(MongoCollection<User> collection) {
        super(collection);
    }
}

package repository;

import com.mongodb.client.MongoCollection;
import model.user.User;

public class UserRepository extends MongoRepository<User> {

    public UserRepository(MongoCollection<User> collection) {
        super(collection);
    }
}

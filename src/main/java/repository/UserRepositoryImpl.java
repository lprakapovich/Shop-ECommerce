package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.NewUser;

public class UserRepositoryImpl implements UserRepository {

    private final MongoCollection<NewUser> userCollection;

    public UserRepositoryImpl(MongoDatabase db) {
     userCollection = db.getCollection("users", NewUser.class);
    }

    @Override
    public String create(NewUser user) {
        userCollection.insertOne(user);
        return null;
    }
}

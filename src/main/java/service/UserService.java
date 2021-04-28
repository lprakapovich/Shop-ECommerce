package service;

import com.mongodb.client.MongoCollection;
import lombok.AllArgsConstructor;
import model.user.User;

@AllArgsConstructor
public class UserService {

    private final MongoCollection<User> usersCollection;

    public void create(User user) {
        usersCollection.insertOne(user);
    }
}

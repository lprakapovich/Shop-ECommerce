package service;

import com.mongodb.client.MongoCollection;
import exception.BadRequestException;
import model.user.User;
import repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService(MongoCollection<User> collection) {
        userRepository = new UserRepository(collection);
    }

    public String create(User user) {
        validateUser(user);
        return userRepository.create(user);
    }

    private void validateUser(User user) {
        if (userRepository.existsByFieldValue("email", user.getEmail())) {
            throw new BadRequestException("User with such an email already exists");
        }
    }
}

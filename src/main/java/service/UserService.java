package service;

import com.mongodb.client.MongoCollection;
import exception.BadRequestException;
import exception.ResourceNotFoundException;
import model.user.User;
import repository.UserRepository;

import java.util.List;

import static api.Message.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class UserService {

    private final UserRepository userRepository;

    public UserService(MongoCollection<User> collection) {
        userRepository = new UserRepository(collection);
    }

    public String create(User user) {
        validateUser(user);
        return userRepository.create(user);
    }

    public User authenticate(String encryptedEmail, String encryptedPassword) {
        List<User> users = userRepository.find(and(eq("password", encryptedPassword), eq("email", encryptedEmail)));
        if (users.size() != 1) {
            throw new BadRequestException(INVALID_USER_CREDENTIALS);
        }

        return users.get(0);
    }

    private void validateUser(User user) {
        if (userRepository.existsByFieldValue("email", user.getEmail())) {
            throw new BadRequestException(USER_DUPLICATED_EMAIL);
        }
    }

    public void updateUserOrderList(String userEmail, String orderId) {
        if (!userRepository.existsByFieldValue("email", userEmail)) {
            throw new ResourceNotFoundException(USER_NOT_FOUND);
        }
        userRepository.updateOrderList(userEmail, orderId);
    }
}

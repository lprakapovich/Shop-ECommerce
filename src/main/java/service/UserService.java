package service;

import com.mongodb.client.MongoCollection;
import exception.BadRequestException;
import model.user.Role;
import model.user.User;
import repository.UserRepository;

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
        User user = userRepository.findOne(and(eq("password", encryptedPassword), eq("email", encryptedEmail)));
        if (user == null) {
            throw new BadRequestException(INVALID_USER_CREDENTIALS);
        }
        return user;
    }

    public boolean isAdmin(String username) {
        User user = userRepository.findOne(eq("email", username));
        if (user == null) {
            throw new BadRequestException(USER_NOT_FOUND);
        }
        return user.getRole().equals(Role.Admin);
    }

    private void validateUser(User user) {
        if (userRepository.existsByFieldValue("email", user.getEmail())) {
            throw new BadRequestException(USER_DUPLICATED_EMAIL);
        }
    }
}

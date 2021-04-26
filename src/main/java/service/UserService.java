package service;

import model.NewUser;
import lombok.AllArgsConstructor;
import repository.UserRepository;

@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public String create(NewUser user) {
        return userRepository.create(user);
    }
}

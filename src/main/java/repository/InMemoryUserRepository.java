package repository;

import model.AppUser;
import model.NewUser;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {

    public static final Map<String, AppUser> USERS_STORE = new ConcurrentHashMap<>();

    @Override
    public String create(NewUser newUser) {
        String id = UUID.randomUUID().toString();
        AppUser user = AppUser.builder()
                .id(id)
                .login(newUser.getLogin())
                .password(newUser.getPassword())
                .build();

        USERS_STORE.put(newUser.getLogin(), user);
        return id;
    }
}

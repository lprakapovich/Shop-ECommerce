package repository;

import model.NewUser;

public interface UserRepository {
    String create(NewUser user);
}

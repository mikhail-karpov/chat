package com.mikhailkarpov.backend.users;

import java.util.Optional;

public interface UserRepository {

  Optional<User> findById(String userId);

  User save(User user);

}

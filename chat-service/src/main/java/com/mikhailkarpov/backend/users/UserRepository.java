package com.mikhailkarpov.backend.users;

import java.util.Optional;

public interface UserRepository {

  boolean existsById(String userId);

  Optional<User> findById(String userId);

  void save(User user);

}

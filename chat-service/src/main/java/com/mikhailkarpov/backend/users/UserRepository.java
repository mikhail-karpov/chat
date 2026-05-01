package com.mikhailkarpov.backend.users;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

  boolean existsById(String userId);

  Optional<User> findById(String userId);

  List<User> findByUsername(String query);

  User save(User user);

}

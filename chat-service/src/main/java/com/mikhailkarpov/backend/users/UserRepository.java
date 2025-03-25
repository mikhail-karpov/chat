package com.mikhailkarpov.backend.users;

import java.util.Optional;

public interface UserRepository {

  Optional<User> findById(String userId);

  void save(User user);

}

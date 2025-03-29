package com.mikhailkarpov.backend.users.memory;

import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {

  private final Map<String, User> users;

  public InMemoryUserRepository() {
    this(new ConcurrentHashMap<>());
  }

  public InMemoryUserRepository(Map<String, User> users) {
    this.users = users;
  }


  @Override
  public Optional<User> findById(String userId) {

    User user = users.get(userId);
    return Optional.ofNullable(user);
  }

  @Override
  public void save(User user) {

    users.put(user.id(), user);
  }

}

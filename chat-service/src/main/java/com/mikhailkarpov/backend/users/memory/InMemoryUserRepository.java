package com.mikhailkarpov.backend.users.memory;

import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserRepository;
import java.util.List;
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
  public boolean existsById(String userId) {

    return users.containsKey(userId);
  }

  @Override
  public Optional<User> findById(String userId) {

    User user = users.get(userId);
    return Optional.ofNullable(user);
  }

  @Override
  public List<User> findByUsername(String query) {

    return users.values().stream().filter(u -> u.username().equals(query)).toList();
  }

  @Override
  public User save(User user) {

    users.put(user.id(), user);
    return user;
  }

}

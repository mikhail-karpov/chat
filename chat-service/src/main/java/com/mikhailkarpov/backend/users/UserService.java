package com.mikhailkarpov.backend.users;

import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public boolean existsById(String userId) {
    return userRepository.existsById(userId);
  }

  @Transactional(readOnly = true)
  @Cacheable(cacheNames = "users", key = "#userId")
  public Optional<User> findById(String userId) {
    return userRepository.findById(userId);
  }

  @Transactional
  @CachePut(cacheNames = "users", key = "#user.id()")
  public User save(User user) {
    return userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public List<User> search(String query) {
    return userRepository.findByUsername(query);
  }
}

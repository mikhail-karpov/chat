package com.mikhailkarpov.backend.users;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public Optional<User> findById(String userId) {
    return userRepository.findById(userId);
  }

  @Transactional
  public void save(User user) {
    userRepository.save(user);
  }

}

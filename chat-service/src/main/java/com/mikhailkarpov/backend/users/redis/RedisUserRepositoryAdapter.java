package com.mikhailkarpov.backend.users.redis;

import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RedisUserRepositoryAdapter implements UserRepository {

  private final RedisUserRepository userRepository;

  public RedisUserRepositoryAdapter(RedisUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Optional<User> findById(String userId) {

    return userRepository.findById(userId)
        .map(UserHash::toDomain);
  }

  @Override
  public void save(User user) {

    UserHash userHash = UserHash.fromDomain(user);
    userRepository.save(userHash);
  }
}

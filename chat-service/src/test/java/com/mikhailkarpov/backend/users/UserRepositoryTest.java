package com.mikhailkarpov.backend.users;

import com.mikhailkarpov.backend.config.IntegrationTest;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void saveAndFindUser() {

    User user = new User("test-id", "test-username");
    userRepository.save(user);
    Optional<User> foundUser = userRepository.findById("test-id");

    Assertions.assertThat(foundUser).hasValueSatisfying(it ->
        Assertions.assertThat(it).isEqualTo(user));
  }

}
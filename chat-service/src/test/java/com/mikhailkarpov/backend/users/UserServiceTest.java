package com.mikhailkarpov.backend.users;

import com.mikhailkarpov.backend.config.IntegrationTest;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

@IntegrationTest
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private CacheManager cacheManager;

  @Test
  void saveAndFindUser() {

    User user = new User("test-id", "test-username");
    userService.save(user);
    Optional<User> foundUser = userService.findById("test-id");

    Assertions.assertThat(foundUser).hasValueSatisfying(it ->
        Assertions.assertThat(it).isEqualTo(user));

    Cache usersCache = cacheManager.getCache("users");
    Assertions.assertThat(usersCache).isNotNull();

    ValueWrapper valueWrapper = usersCache.get("test-id");
    Assertions.assertThat(valueWrapper).isNotNull();
    Assertions.assertThat((User) valueWrapper.get()).isEqualTo(user);
  }

}
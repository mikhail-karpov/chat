package com.mikhailkarpov.backend.config;

import com.mikhailkarpov.backend.users.UserService;
import com.mikhailkarpov.backend.users.memory.InMemoryUserRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@TestConfiguration
@Import(SecurityConfig.class)
public class SecurityTestConfig {

  @Bean
  JwtDecoder jwtDecoder() {
    return Mockito.mock(JwtDecoder.class);
  }

  @Bean
  @ConditionalOnMissingBean
  public UserService userRepository() {
    return new UserService(new InMemoryUserRepository());
  }

}
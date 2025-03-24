package com.mikhailkarpov.backend.config;

import com.mikhailkarpov.backend.users.UserRepository;
import com.mikhailkarpov.backend.users.memory.InMemoryUserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@TestConfiguration
@Import(SecurityConfig.class)
public class SecurityTestConfig {

  @MockitoBean
  private ClientRegistrationRepository clientRegistrationRepository;

  @Bean
  @ConditionalOnMissingBean
  public UserRepository userRepository() {
    return new InMemoryUserRepository();
  }

}
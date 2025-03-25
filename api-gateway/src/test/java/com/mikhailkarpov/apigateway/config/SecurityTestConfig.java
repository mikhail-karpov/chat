package com.mikhailkarpov.apigateway.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

@TestConfiguration
@Import(SecurityConfig.class)
public class SecurityTestConfig {

  @Bean
  ReactiveClientRegistrationRepository reactiveClientRegistrationRepository() {

    return Mockito.mock(ReactiveClientRegistrationRepository.class);
  }

}
package com.mikhailkarpov.backend.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@TestConfiguration
@Import(SecurityConfig.class)
public class SecurityTestConfig {

  @MockitoBean
  private ClientRegistrationRepository clientRegistrationRepository;

}
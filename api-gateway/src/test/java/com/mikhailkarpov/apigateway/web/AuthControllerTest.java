package com.mikhailkarpov.apigateway.web;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOidcLogin;

import com.mikhailkarpov.apigateway.config.SecurityTestConfig;
import com.mikhailkarpov.apigateway.web.AuthController.CurrentUserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.JwtMutator;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.OidcLoginMutator;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = AuthController.class)
@Import(SecurityTestConfig.class)
class AuthControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void unauthorized() {

    webTestClient.get()
        .uri("/api/v1/auth")
        .exchange()
        .expectStatus()
        .isUnauthorized();
  }

  @Test
  void authorizedOidcLogin() {

    OidcLoginMutator oidcLogin = mockOidcLogin().idToken(token -> token
        .subject("test-subject")
        .claim("preferred_username", "test-username")
    );

    webTestClient.mutateWith(oidcLogin)
        .get()
        .uri("/api/v1/auth")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(CurrentUserResponse.class)
        .isEqualTo(new CurrentUserResponse("test-subject", "test-username"));
  }

  @Test
  void authorizedJwt() {

    JwtMutator jwtMutator = mockJwt().jwt(jwt -> jwt
        .subject("test-subject")
        .claim("preferred_username", "test-username"));

    webTestClient.mutateWith(jwtMutator)
        .get()
        .uri("/api/v1/auth")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(CurrentUserResponse.class)
        .isEqualTo(new CurrentUserResponse("test-subject", "test-username"));
  }

}
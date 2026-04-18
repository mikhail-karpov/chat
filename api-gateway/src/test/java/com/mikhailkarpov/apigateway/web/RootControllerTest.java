package com.mikhailkarpov.apigateway.web;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOidcLogin;

import com.mikhailkarpov.apigateway.config.SecurityTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(
    controllers = RootController.class,
    properties = "app.frontend-url = http://localhost:3000"
)
@Import(SecurityTestConfig.class)
class RootControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void root() {

    webTestClient.mutateWith(mockOidcLogin())
        .get()
        .uri("/")
        .exchange()
        .expectStatus()
        .is3xxRedirection()
        .expectHeader()
        .location("http://localhost:3000");
  }

  @Test
  void unauthorized() {

    webTestClient.get()
        .uri("/")
        .exchange()
        .expectStatus()
        .isUnauthorized();
  }

}
package com.mikhailkarpov.apigateway.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(
    controllers = RootController.class,
    properties = "app.frontend-url = http://localhost:3000"
)
class RootControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void root() {

    webTestClient.get()
        .uri("/")
        .exchange()
        .expectStatus()
        .is3xxRedirection()
        .expectHeader()
        .location("http://localhost:3000");
  }

}
package com.mikhailkarpov.apigateway.web;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@ConditionalOnProperty(value = "app.frontend-url")
public class RootController {

  @Value("${app.frontend-url}")
  private String frontendUrl;

  @GetMapping("/")
  public Mono<Void> root(ServerHttpResponse response) {
    response.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
    response.getHeaders().setLocation(URI.create(this.frontendUrl));
    return response.setComplete();
  }

}

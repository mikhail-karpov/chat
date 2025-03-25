package com.mikhailkarpov.apigateway.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AuthController {

  @GetMapping("/api/v1/auth")
  public Mono<CurrentUserResponse> getCurrentUser(@AuthenticationPrincipal OidcUser user) {

    String id = user.getSubject();
    String username = user.getClaimAsString("preferred_username");
    return Mono.just(new CurrentUserResponse(id, username));
  }

  public record CurrentUserResponse(String id, String username) {
  }

}

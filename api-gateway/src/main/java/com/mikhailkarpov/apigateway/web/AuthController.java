package com.mikhailkarpov.apigateway.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AuthController {

  private static final Logger log = LoggerFactory.getLogger(AuthController.class);

  @GetMapping("/api/v1/auth")
  public Mono<CurrentUserResponse> getCurrentUser(Authentication authentication) {

    String id, username;

    if (authentication.getPrincipal() instanceof OidcUser user) {
      id = user.getSubject();
      username = user.getPreferredUsername();
    } else if (authentication instanceof JwtAuthenticationToken token) {
      id = token.getToken().getSubject();
      username = token.getToken().getClaimAsString("preferred_username");
    } else {
      log.warn("Unsupported authentication type: {}", authentication.getClass().getName());
      throw new InsufficientAuthenticationException("Unsupported authentication type");
    }
    return Mono.just(new CurrentUserResponse(id, username));
  }

  public record CurrentUserResponse(String id, String username) {
  }

}

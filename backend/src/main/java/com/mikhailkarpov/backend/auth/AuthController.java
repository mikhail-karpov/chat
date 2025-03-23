package com.mikhailkarpov.backend.auth;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  @GetMapping("/api/v1/auth")
  public CurrentUserResponse getCurrentUser(@AuthenticationPrincipal OidcUser user) {

    String id = user.getSubject();
    String username = user.getClaimAsString("preferred_username");
    return new CurrentUserResponse(id, username);
  }

  public record CurrentUserResponse(String id, String username) {
  }

}

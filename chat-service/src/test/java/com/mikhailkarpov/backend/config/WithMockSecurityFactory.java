package com.mikhailkarpov.backend.config;

import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.jwt.ChatAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockSecurityFactory implements WithSecurityContextFactory<WithMockChatUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockChatUser annotation) {

    String subject = annotation.id();
    String username = annotation.username();
    String displayName = annotation.displayName();

    Jwt jwt = Jwt.withTokenValue("jwt-token")
        .header("typ", "JWT")
        .subject(subject)
        .claim("preferred_username", username)
        .claim("name", displayName)
        .build();

    User user = new User(subject, username, displayName);
    ChatAuthenticationToken authentication = new ChatAuthenticationToken(jwt, user);
    authentication.setAuthenticated(true);

    SecurityContext context = SecurityContextHolder.getContextHolderStrategy().createEmptyContext();
    context.setAuthentication(authentication);
    return context;
  }

}

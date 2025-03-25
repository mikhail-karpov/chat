package com.mikhailkarpov.backend.users.jwt;

import com.mikhailkarpov.backend.users.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;

public class ChatAuthenticationToken extends AbstractAuthenticationToken {

  private final Jwt jwt;
  private final User user;

  public ChatAuthenticationToken(Jwt jwt, User user) {
    super(AuthorityUtils.NO_AUTHORITIES);
    this.jwt = jwt;
    this.user = user;
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return this.jwt;
  }

  @Override
  public Object getPrincipal() {
    return this.user;
  }
}

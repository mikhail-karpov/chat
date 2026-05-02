package com.mikhailkarpov.backend.users.jwt;

import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

public class ChatAuthenticationTokenConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private final UserService userService;

  public ChatAuthenticationTokenConverter(UserService userService) {
    this.userService = userService;
  }

  @Override
  public ChatAuthenticationToken convert(Jwt jwt) {

    var userId = jwt.getSubject();
    var username = jwt.getClaimAsString("preferred_username");
    var displayName = jwt.getClaimAsString("name");

    var user = userService.findById(userId)
        .filter(u -> u.username().equals(username))
        .filter(u -> u.displayName().equals(displayName))
        .orElse(null);

    if (user == null) {
      user = userService.save(new User(userId, username, displayName));
    }

    return new ChatAuthenticationToken(jwt, user);
  }


}

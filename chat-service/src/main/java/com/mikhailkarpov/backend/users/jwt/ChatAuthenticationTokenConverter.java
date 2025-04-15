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

    String userId = jwt.getSubject();
    String username = jwt.getClaimAsString("preferred_username");

    User user = userService.findById(userId).orElseGet(() -> {
      User newUser = new User(userId, username);
      userService.save(newUser);
      return newUser;
    });

    return new ChatAuthenticationToken(jwt, user);
  }


}

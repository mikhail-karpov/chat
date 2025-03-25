package com.mikhailkarpov.backend.users.jwt;

import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

public class ChatAuthenticationTokenConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private final UserRepository userRepository;

  public ChatAuthenticationTokenConverter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }


  @Override
  public ChatAuthenticationToken convert(Jwt jwt) {

    String userId = jwt.getSubject();
    String username = jwt.getClaimAsString("preferred_username");

    User user = userRepository.findById(userId).orElseGet(() -> {
      User newUser = new User(userId, username);
      userRepository.save(newUser);
      return newUser;
    });

    return new ChatAuthenticationToken(jwt, user);
  }


}

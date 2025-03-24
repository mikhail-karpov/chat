package com.mikhailkarpov.backend.auth;

import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class CustomOidcUserService extends OidcUserService {

  private static final Logger log = LoggerFactory.getLogger(CustomOidcUserService.class);

  private final UserRepository userRepository;

  public CustomOidcUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

    OidcUser oidcUser = super.loadUser(userRequest);
    if (!userRepository.existsById(oidcUser.getSubject())) {
      User user = createUser(oidcUser);
      log.info("Saving user {}", user);
      userRepository.save(user);
    }
    return oidcUser;
  }

  private static User createUser(OidcUser oidcUser) {

    String userId = oidcUser.getSubject();
    String username = oidcUser.getPreferredUsername();
    return new User(userId, username);
  }

}

package com.mikhailkarpov.backend.auth;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mikhailkarpov.backend.config.SecurityTestConfig;
import java.util.function.Consumer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.core.oidc.OidcIdToken.Builder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AuthController.class)
@Import(SecurityTestConfig.class)
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;


  @Nested
  class GetCurrentUserTests {

    @Test
    void unauthorized() throws Exception {

      mockMvc.perform(get("/api/v1/auth"))
          .andExpect(status().isUnauthorized());
    }

    @Test
    void authorized() throws Exception {

      Consumer<Builder> token = idToken -> idToken
          .subject("test-subject")
          .claim("preferred_username", "test-username");

      mockMvc.perform(get("/api/v1/auth")
              .with(oidcLogin().idToken(token)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("id").value("test-subject"))
          .andExpect(jsonPath("username").value("test-username"));
    }
  }

}
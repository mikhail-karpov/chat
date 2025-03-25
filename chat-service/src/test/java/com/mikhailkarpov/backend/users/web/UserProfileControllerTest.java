package com.mikhailkarpov.backend.users.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mikhailkarpov.backend.config.SecurityTestConfig;
import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserProfileController.class)
@Import(SecurityTestConfig.class)
@WithMockUser
class UserProfileControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Test
  void userProfileOk() throws Exception {

    userRepository.save(new User("test-id", "test-username"));

    mockMvc.perform(get("/api/v1/users/test-id/profile"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").value("test-id"))
        .andExpect(jsonPath("username").value("test-username"));
  }

  @Test
  void userProfileNotFound() throws Exception {

    mockMvc.perform(get("/api/v1/users/not-found/profile"))
        .andExpect(status().isNotFound());
  }

}
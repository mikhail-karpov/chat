package com.mikhailkarpov.backend.users.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mikhailkarpov.backend.config.SecurityTestConfig;
import com.mikhailkarpov.backend.config.WithMockChatUser;
import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserProfileController.class)
@Import(SecurityTestConfig.class)
@WithMockChatUser
class UserProfileControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;


  @Nested
  class GetUserProfileTests {

    @Test
    void userProfileOk() throws Exception {

      when(userService.findById("test-id"))
          .thenReturn(Optional.of(new User("test-id", "test-username")));

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


  @Nested
  class SearchUsersTests {

    @Test
    void searchUsersOk() throws Exception {

      when(userService.search("test-user")).thenReturn(List.of(
          new User("test-id-1", "test-user-1"),
          new User("test-id-2", "test-user-2")
      ));

      mockMvc.perform(get("/api/v1/users/search?query=test-user"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.users.length()").value(2))
          .andExpect(jsonPath("users[0].id").value("test-id-1"))
          .andExpect(jsonPath("users[0].username").value("test-user-1"))
          .andExpect(jsonPath("users[1].id").value("test-id-2"))
          .andExpect(jsonPath("users[1].username").value("test-user-2"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "", // empty
        "ab", // too short
    })
    void searchUsersQueryBadRequest(String invalidQuery) throws Exception {

      mockMvc.perform(get("/api/v1/users/search?query={query}", invalidQuery))
          .andExpect(status().isBadRequest());
    }

    @Test
    void searchUsersQueryEmptyBadRequest() throws Exception {

      mockMvc.perform(get("/api/v1/users/search"))
          .andExpect(status().isBadRequest());
    }
  }
}
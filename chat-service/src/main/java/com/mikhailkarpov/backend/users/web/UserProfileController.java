package com.mikhailkarpov.backend.users.web;

import com.mikhailkarpov.backend.config.OpenApiSecurityScheme;
import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@OpenApiSecurityScheme
public class UserProfileController {

  private final UserService userService;

  public UserProfileController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/search")
  public ResponseEntity<UserProfileListResponse> searchUsers(
      @AuthenticationPrincipal User user, @Valid @ParameterObject UserSearchRequest request) {

    List<User> users = userService.search(request.query())
        .stream()
        .filter(u -> !u.username().equals(user.username()))
        .toList();

    return ResponseEntity.ok(UserProfileListResponse.from(users));
  }

  @GetMapping("/{userId}/profile")
  public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable String userId) {

    UserProfileResponse profile = userService.findById(userId)
        .map(UserProfileResponse::from)
        .orElse(null);

    return ResponseEntity.ofNullable(profile);
  }

}

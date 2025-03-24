package com.mikhailkarpov.backend.users.web;

import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserProfileController {

  private final UserRepository userRepository;

  public UserProfileController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/{userId}/profile")
  public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable String userId) {

    UserProfileResponse profile = userRepository.findById(userId)
        .map(UserProfileResponse::of)
        .orElse(null);

    return ResponseEntity.ofNullable(profile);
  }

  public record UserProfileResponse(String id, String username) {

    public static UserProfileResponse of(User user) {
      return new UserProfileResponse(user.id(), user.username());
    }
  }
}

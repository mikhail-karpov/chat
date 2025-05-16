package com.mikhailkarpov.backend.users.web;

import com.mikhailkarpov.backend.users.User;
import java.util.List;

public record UserProfileListResponse(List<UserProfileResponse> users) {

  public static UserProfileListResponse from(List<User> users) {
    return new UserProfileListResponse(users.stream().map(UserProfileResponse::from).toList());
  }
}

package com.mikhailkarpov.backend.users.web;

import com.mikhailkarpov.backend.users.User;

public record UserProfileResponse(String id, String username) {

  public static UserProfileResponse from(User user) {
    return new UserProfileResponse(user.id(), user.username());
  }
}

package com.mikhailkarpov.backend.centrifugo.client;

import org.springframework.lang.Nullable;

public record CentrifugoPublishResponse(@Nullable Error error) {

  public record Error(Integer code, String message) {
  }

}

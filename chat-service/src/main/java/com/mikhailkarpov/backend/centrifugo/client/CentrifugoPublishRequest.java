package com.mikhailkarpov.backend.centrifugo.client;

import java.util.UUID;

public record CentrifugoPublishRequest<T>(
    String idempotencyKey,
    String channel,
    T data
) {

  public CentrifugoPublishRequest(String channel, T data) {
    this(UUID.randomUUID().toString(), channel, data);
  }

}

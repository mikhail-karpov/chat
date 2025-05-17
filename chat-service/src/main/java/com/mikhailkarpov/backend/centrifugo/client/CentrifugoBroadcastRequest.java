package com.mikhailkarpov.backend.centrifugo.client;

import java.util.UUID;

public record CentrifugoBroadcastRequest<T>(String idempotencyKey, String[] channels, T data) {

  public CentrifugoBroadcastRequest(String[] channels, T data) {
    this(UUID.randomUUID().toString(), channels, data);
  }

}

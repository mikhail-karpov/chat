package com.mikhailkarpov.backend.messages;

import java.time.Instant;
import java.util.UUID;

public record Message(String id, String userId, String text, Instant createdAt) {

  public Message(String userId, String text) {
    this(UUID.randomUUID().toString(), userId, text, Instant.now());
  }

}

package com.mikhailkarpov.backend.messages;

import java.time.Instant;
import java.util.UUID;

public record Message(UUID id, String userId, String text, Instant createdAt) {

  public Message(String userId, String text) {
    this(UUID.randomUUID(), userId, text, Instant.now());
  }

}

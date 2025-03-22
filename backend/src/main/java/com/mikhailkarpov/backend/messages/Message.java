package com.mikhailkarpov.backend.messages;

import java.time.Instant;
import java.util.UUID;

public record Message(String id, String text, Instant createdAt) {

  public Message(String text) {
    this(UUID.randomUUID().toString(), text, Instant.now());
  }

}

package com.mikhailkarpov.backend.messages;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Message {

  @Builder.Default
  private final UUID id = UUID.randomUUID();

  private final UUID conversationId;

  private final String userId;

  private final String text;

  @Builder.Default
  private final Instant createdAt = Instant.now();

  public Message(UUID id, UUID conversationId, String userId, String text, Instant createdAt) {
    this.id = id;
    this.conversationId = conversationId;
    this.userId = userId;
    this.text = text;
    this.createdAt = createdAt;
  }

}

package com.mikhailkarpov.backend.conversation;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class ConversationParticipant {

  private final UUID conversationId;
  private final String userId;
  private final boolean isBlocked;

  public ConversationParticipant(UUID conversationId, String userId) {
    this(conversationId, userId, false);
  }

  public ConversationParticipant(UUID conversationId, String userId, boolean isBlocked) {
    this.conversationId = conversationId;
    this.userId = userId;
    this.isBlocked = isBlocked;
  }
}

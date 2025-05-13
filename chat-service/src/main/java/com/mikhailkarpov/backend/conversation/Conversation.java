package com.mikhailkarpov.backend.conversation;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Conversation {

  private final UUID id;
  private final boolean isPublic;
  private final boolean isGroup;

  public Conversation() {
    this(UUID.randomUUID(), false, false);
  }

  public Conversation(UUID id, boolean isPublic, boolean isGroup) {
    this.id = id;
    this.isPublic = isPublic;
    this.isGroup = isGroup;
  }
}

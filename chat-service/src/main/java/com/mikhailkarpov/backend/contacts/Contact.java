package com.mikhailkarpov.backend.contacts;

import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Contact {

  private final UUID conversationId;
  private final String userId;
  private final String contactUserId;
  private ContactStatus status;

  @Builder
  public Contact(UUID conversationId, String userId, String contactUserId, ContactStatus status) {
    this.conversationId = conversationId;
    this.userId = userId;
    this.contactUserId = contactUserId;
    this.status = status;
  }

  public void approve() {
    if (status == ContactStatus.BLOCKED) {
      status = ContactStatus.APPROVED;
    }
  }

  public void block() {
    if (status == ContactStatus.APPROVED) {
      status = ContactStatus.BLOCKED;
    }
  }

  public boolean isApproved() {
    return status == ContactStatus.APPROVED;
  }

}

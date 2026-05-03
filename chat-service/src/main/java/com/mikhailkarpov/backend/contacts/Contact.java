package com.mikhailkarpov.backend.contacts;

import com.mikhailkarpov.backend.users.User;
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
  private final String contactUsername;
  private String contactDisplayName;
  private ContactStatus status;

  @Builder
  public Contact(UUID conversationId, String userId, User contactUser, ContactStatus status) {
    this.conversationId = conversationId;
    this.userId = userId;
    this.contactUserId = contactUser.id();
    this.contactUsername = contactUser.username();
    this.contactDisplayName = contactUser.displayName();
    this.status = status;
  }

  public void updateDisplayName(String contactDisplayName) {
    this.contactDisplayName = contactDisplayName;
  }

  public void approve() {
    status = ContactStatus.APPROVED;
  }

  public void block() {
    status = ContactStatus.BLOCKED;
  }

  public boolean isApproved() {
    return status == ContactStatus.APPROVED;
  }

}

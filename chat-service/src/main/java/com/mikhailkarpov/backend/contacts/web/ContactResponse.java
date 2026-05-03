package com.mikhailkarpov.backend.contacts.web;

import com.mikhailkarpov.backend.contacts.Contact;
import com.mikhailkarpov.backend.contacts.ContactStatus;
import java.util.UUID;

public record ContactResponse(
    UUID conversationId,
    String id, String username, String displayName,
    ContactStatus status) {

  public static ContactResponse from(Contact contact) {
    return new ContactResponse(
        contact.getConversationId(),
        contact.getContactUserId(),
        contact.getContactUsername(),
        contact.getContactDisplayName(),
        contact.getStatus()
    );
  }
}

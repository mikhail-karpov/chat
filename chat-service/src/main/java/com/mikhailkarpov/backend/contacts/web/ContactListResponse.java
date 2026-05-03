package com.mikhailkarpov.backend.contacts.web;

import com.mikhailkarpov.backend.contacts.Contact;
import java.util.List;

public record ContactListResponse(List<ContactResponse> contacts) {

  public static ContactListResponse from(List<Contact> contacts) {
    return new ContactListResponse(contacts.stream().map(ContactResponse::from).toList());
  }

}
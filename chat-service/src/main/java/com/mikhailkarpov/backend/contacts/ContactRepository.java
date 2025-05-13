package com.mikhailkarpov.backend.contacts;

import java.util.List;
import java.util.Optional;

public interface ContactRepository {

  void addContact(Contact contact);

  void updateContact(Contact contact);

  Optional<Contact> findContact(String userId, String contactUserId);

  List<ContactView> findContacts(String userId, Iterable<ContactStatus> status);

}

package com.mikhailkarpov.backend.contacts;

import static org.assertj.core.api.Assertions.*;

import com.mikhailkarpov.backend.config.IntegrationTest;
import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserNotFoundException;
import com.mikhailkarpov.backend.users.UserService;
import java.util.EnumSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@Transactional
@Rollback
class ContactServiceTest {

  private final User user1 = new User("user-1", "username-1", "Test 1");
  private final User user2 = new User("user-2", "username-2", "Test 2");
  private final User user3 = new User("user-3", "username-3", "Test 3");

  @Autowired
  private UserService userService;

  @Autowired
  private ContactService contactService;

  @BeforeEach
  void setUp() {
    userService.save(user1);
    userService.save(user2);
    userService.save(user3);
  }

  @Test
  void addContact() {

    contactService.addContact(new AddContactCommand("user-1", "user-2"));
    contactService.addContact(new AddContactCommand("user-1", "user-3"));

    var query = new ContactListQuery(user1.id(), EnumSet.allOf(ContactStatus.class));
    assertThat(contactService.listContacts(query))
        .hasSize(2)
        .map(Contact::getContactUsername)
        .containsExactlyInAnyOrder("username-2", "username-3");
  }

  @Test
  void addContactCreatesPendingContact() {

    contactService.addContact(new AddContactCommand("user-1", "user-2"));

    assertThat(contactService.getContact("user-2", "user-1"))
        .returns("user-1", Contact::getContactUserId)
        .returns("Test 1", Contact::getContactDisplayName)
        .returns(ContactStatus.PENDING, Contact::getStatus)
        .matches(contact -> contact.getConversationId() != null);
  }

  @Test
  void addYourselfToContactNotAllowed() {

    var command = new AddContactCommand("user-1", "user-1");
    assertThatThrownBy(() -> contactService.addContact(command))
        .isInstanceOf(ContactNotAllowedException.class);
  }

  @Test
  void addNonExistingUserToContactNotAllowed() {

    var command = new AddContactCommand("user-1", "not-found");
    assertThatThrownBy(() -> contactService.addContact(command))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void blockContact() {

    contactService.addContact(new AddContactCommand("user-1", "user-2"));
    contactService.editContact(new EditContactCommand("user-1", "user-2", null, ContactStatus.BLOCKED));

    assertThat(contactService.getContact("user-1", "user-2"))
      .returns(ContactStatus.BLOCKED, Contact::getStatus);
  }

  @Test
  void blockContactNotFound() {

    var command = new EditContactCommand("user-1", "user-2", null, ContactStatus.BLOCKED);

    assertThatThrownBy(() -> contactService.editContact(command))
        .isInstanceOf(ContactNotFoundException.class);
  }

  @Test
  void unblockContact() {

    contactService.addContact(new AddContactCommand("user-1", "user-2"));
    contactService.editContact(new EditContactCommand("user-1", "user-2", null, ContactStatus.BLOCKED));
    contactService.editContact(new EditContactCommand("user-1", "user-2", null, ContactStatus.APPROVED));

    var query = new ContactListQuery(user1.id(), EnumSet.of(ContactStatus.APPROVED));
    assertThat(contactService.listContacts(query))
        .hasSize(1)
        .map(Contact::getContactUserId)
        .containsExactlyInAnyOrder("user-2");
  }

  @Test
  void editContactDisplayName() {

    contactService.addContact(new AddContactCommand("user-1", "user-2"));
    contactService.editContact(new EditContactCommand("user-1", "user-2", "Test User", null));

    var query = new ContactListQuery(user1.id(), EnumSet.of(ContactStatus.APPROVED));
    assertThat(contactService.listContacts(query))
        .hasSize(1)
        .map(Contact::getContactDisplayName)
        .containsExactlyInAnyOrder("Test User");
  }
}
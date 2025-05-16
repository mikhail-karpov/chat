package com.mikhailkarpov.backend.contacts;

import static org.assertj.core.api.Assertions.*;

import com.mikhailkarpov.backend.config.IntegrationTest;
import com.mikhailkarpov.backend.users.User;
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

  private final User user1 = new User("user-1", "username-1");
  private final User user2 = new User("user-2", "username-2");
  private final User user3 = new User("user-3", "username-3");

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

    contactService.addContact(new AddContactCommand(user1, user2));
    contactService.addContact(new AddContactCommand(user1, user3));

    var query = new ContactListQuery(user1.id(), EnumSet.allOf(ContactStatus.class));
    assertThat(contactService.listContacts(query))
        .hasSize(2)
        .map(ContactView::username)
        .containsExactlyInAnyOrder("username-2", "username-3");
  }

  @Test
  void addContactCreatesPendingContact() {

    contactService.addContact(new AddContactCommand(user1, user2));

    var query = new ContactListQuery(user2.id(), EnumSet.of(ContactStatus.PENDING));
    assertThat(contactService.listContacts(query))
        .hasSize(1)
        .map(ContactView::username)
        .containsExactlyInAnyOrder("username-1");
  }

  @Test
  void addYourselfToContactNotAllowed() {

    var command = new AddContactCommand(user1, user1);
    assertThatThrownBy(() -> contactService.addContact(command))
        .isInstanceOf(ContactNotAllowedException.class);
  }

  @Test
  void blockContact() {

    contactService.addContact(new AddContactCommand(user1, user2));
    contactService.blockContact(new BlockContactCommand(user1, user2));

    var query = new ContactListQuery(user1.id(), EnumSet.of(ContactStatus.APPROVED));
    assertThat(contactService.listContacts(query)).isEmpty();
  }

  @Test
  void blockContactNotFound() {

    assertThatThrownBy(() -> contactService.blockContact(new BlockContactCommand(user1, user2)))
        .isInstanceOf(ContactNotFoundException.class);
  }
}
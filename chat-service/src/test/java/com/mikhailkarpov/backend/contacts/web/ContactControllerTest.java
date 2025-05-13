package com.mikhailkarpov.backend.contacts.web;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mikhailkarpov.backend.config.SecurityTestConfig;
import com.mikhailkarpov.backend.config.WithMockChatUser;
import com.mikhailkarpov.backend.contacts.AddContactCommand;
import com.mikhailkarpov.backend.contacts.BlockContactCommand;
import com.mikhailkarpov.backend.contacts.ContactListQuery;
import com.mikhailkarpov.backend.contacts.ContactService;
import com.mikhailkarpov.backend.contacts.ContactStatus;
import com.mikhailkarpov.backend.contacts.ContactView;
import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserService;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ContactController.class)
@Import(SecurityTestConfig.class)
@WithMockChatUser
class ContactControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ContactService contactService;

  @MockitoBean
  private UserService userService;

  @Nested
  class ListContactsTests {

    private final List<ContactView> contacts = List.of(
        new ContactView(
            UUID.fromString("de761950-6752-4a71-ad8d-d95ca066837d"),
            "contact-1",
            "username-1",
            ContactStatus.APPROVED),
        new ContactView(
            UUID.fromString("62bf87ad-aff7-4476-a6d7-67fd92a5cd5e"),
            "contact-2",
            "username-2",
            ContactStatus.PENDING)
    );

    @Test
    void listContactsOk() throws Exception {

      var statuses = EnumSet.of(ContactStatus.APPROVED);
      var query = new ContactListQuery("test-user-id", statuses);

      when(contactService.listContacts(query))
          .thenReturn(contacts);

      mockMvc.perform(get("/api/v1/contacts"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.contacts.length()").value(2))
          .andExpect(jsonPath("contacts[0].conversationId").value("de761950-6752-4a71-ad8d-d95ca066837d"))
          .andExpect(jsonPath("contacts[0].id").value("contact-1"))
          .andExpect(jsonPath("contacts[0].username").value("username-1"))
          .andExpect(jsonPath("contacts[0].status").value("APPROVED"))
          .andExpect(jsonPath("contacts[1].conversationId").value("62bf87ad-aff7-4476-a6d7-67fd92a5cd5e"))
          .andExpect(jsonPath("contacts[1].id").value("contact-2"))
          .andExpect(jsonPath("contacts[1].username").value("username-2"))
          .andExpect(jsonPath("contacts[1].status").value("PENDING"));
    }

    @Test
    void listEmptyContactsStatusesOk() throws Exception {

      var statuses = EnumSet.of(ContactStatus.PENDING, ContactStatus.APPROVED);
      var query = new ContactListQuery("test-user-id", statuses);

      when(contactService.listContacts(query))
          .thenReturn(List.of(contacts.get(1)));

      mockMvc.perform(get("/api/v1/contacts?statuses=APPROVED,PENDING"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.contacts.length()").value(1))
          .andExpect(jsonPath("contacts[0].conversationId").value("62bf87ad-aff7-4476-a6d7-67fd92a5cd5e"))
          .andExpect(jsonPath("contacts[0].id").value("contact-2"))
          .andExpect(jsonPath("contacts[0].username").value("username-2"))
          .andExpect(jsonPath("contacts[0].status").value("PENDING"));
    }

    @Test
    void listEmptyContactsOk() throws Exception {

      when(contactService.listContacts(any()))
          .thenReturn(List.of());

      mockMvc.perform(get("/api/v1/contacts"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.contacts.length()").value(0));
    }
  }


  @Nested
  class AddContactTests {

    @Test
    void addContactNotFound() throws Exception {

      mockMvc.perform(post("/api/v1/contacts/not-found"))
          .andExpect(status().isNotFound());

      verifyNoInteractions(contactService);
    }

    @Test
    void addContactOk() throws Exception {

      User user = new User("test-user-id", "test-username");
      User contact = new User("contact-user-id", "contact-username");

      when(userService.findById("contact-user-id"))
          .thenReturn(Optional.of(contact));

      mockMvc.perform(post("/api/v1/contacts/contact-user-id"))
          .andExpect(status().isOk());

      verify(contactService)
          .addContact(new AddContactCommand(user, contact));
    }
  }


  @Nested
  class BlockContactTests {

    @Test
    void blockContactNotFound() throws Exception {

      mockMvc.perform(post("/api/v1/contacts/not-found/block"))
          .andExpect(status().isNotFound());

      verifyNoInteractions(contactService);
    }

    @Test
    void blockContactOk() throws Exception {

      User user = new User("test-user-id", "test-username");
      User contact = new User("contact-user-id", "contact-username");

      when(userService.findById("contact-user-id"))
          .thenReturn(Optional.of(contact));

      mockMvc.perform(post("/api/v1/contacts/contact-user-id/block"))
          .andExpect(status().isOk());

      verify(contactService)
          .blockContact(new BlockContactCommand(user, contact));
    }
  }
}
package com.mikhailkarpov.backend.contacts.web;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mikhailkarpov.backend.config.SecurityTestConfig;
import com.mikhailkarpov.backend.config.WithMockChatUser;
import com.mikhailkarpov.backend.contacts.AddContactCommand;
import com.mikhailkarpov.backend.contacts.Contact;
import com.mikhailkarpov.backend.contacts.ContactListQuery;
import com.mikhailkarpov.backend.contacts.ContactNotAllowedException;
import com.mikhailkarpov.backend.contacts.ContactNotFoundException;
import com.mikhailkarpov.backend.contacts.ContactService;
import com.mikhailkarpov.backend.contacts.ContactStatus;
import com.mikhailkarpov.backend.contacts.EditContactCommand;
import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserNotFoundException;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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

  @Nested
  class ListContactsTests {

    private final List<Contact> contacts = List.of(
        new Contact(
            UUID.fromString("de761950-6752-4a71-ad8d-d95ca066837d"),
            "user-id",
            new User("contact-1", "username-1", "User 1"),
            ContactStatus.APPROVED),
        new Contact(
            UUID.fromString("62bf87ad-aff7-4476-a6d7-67fd92a5cd5e"),
            "user-id",
            new User("contact-2", "username-2", "User 2"),
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
          .andExpect(jsonPath("contacts[0].displayName").value("User 1"))
          .andExpect(jsonPath("contacts[0].status").value("APPROVED"))
          .andExpect(jsonPath("contacts[1].conversationId").value("62bf87ad-aff7-4476-a6d7-67fd92a5cd5e"))
          .andExpect(jsonPath("contacts[1].id").value("contact-2"))
          .andExpect(jsonPath("contacts[1].username").value("username-2"))
          .andExpect(jsonPath("contacts[1].displayName").value("User 2"))
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
          .andExpect(jsonPath("contacts[0].displayName").value("User 2"))
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
  class GetContactTests {

    private static final String URL = "/api/v1/contacts/contact-user-id";


    @Test
    void getContactOk() throws Exception {

      var contact = new Contact(
          UUID.fromString("de761950-6752-4a71-ad8d-d95ca066837d"),
          "user-id",
          new User("contact-user-id", "username-1", "User 1"),
          ContactStatus.BLOCKED);

      Mockito.when(contactService.getContact("test-user-id", "contact-user-id"))
              .thenReturn(contact);

      mockMvc.perform(get(URL))
          .andExpect(status().isOk())
          .andExpect(jsonPath("conversationId").value("de761950-6752-4a71-ad8d-d95ca066837d"))
          .andExpect(jsonPath("id").value("contact-user-id"))
          .andExpect(jsonPath("username").value("username-1"))
          .andExpect(jsonPath("displayName").value("User 1"))
          .andExpect(jsonPath("status").value("BLOCKED"));
    }

    @Test
    void getContactNotFound() throws Exception {

      Mockito.doThrow(ContactNotFoundException.class)
              .when(contactService).getContact("test-user-id", "contact-user-id");

      mockMvc.perform(get(URL))
          .andExpect(status().isNotFound());
    }
  }


  @Nested
  class AddContactTests {

    private static final String URL = "/api/v1/contacts/contact-user-id";

    private static final AddContactCommand COMMAND =
        new AddContactCommand("test-user-id", "contact-user-id");

    @Test
    void addContactUserNotFound() throws Exception {

      Mockito.doThrow(UserNotFoundException.class)
          .when(contactService).addContact(COMMAND);

      mockMvc.perform(post(URL))
          .andExpect(status().isNotFound());
    }

    @Test
    void addContactNotAllowed() throws Exception {

      Mockito.doThrow(ContactNotAllowedException.class)
          .when(contactService).addContact(COMMAND);

      mockMvc.perform(post(URL))
          .andExpect(status().isBadRequest());
    }

    @Test
    void addContactOk() throws Exception {

      mockMvc.perform(post(URL))
          .andExpect(status().isOk());

      verify(contactService)
          .addContact(COMMAND);
    }
  }


  @Nested
  class EditContactTests {

    private static final String URL = "/api/v1/contacts/contact-user-id";

    @ParameterizedTest
    @ValueSource(strings = {
        """
          {
            "displayName": "ab",
            "status": "BLOCKED"
          }
        """,
    })
    void blockContactBadRequest(String invalidRequest) throws Exception {

      mockMvc.perform(put(URL)
              .contentType(MediaType.APPLICATION_JSON)
              .content(invalidRequest))
          .andExpect(status().isBadRequest());

      Mockito.verifyNoInteractions(contactService);
    }

    @Test
    void blockContactNotFound() throws Exception {

      Mockito.doThrow(ContactNotFoundException.class)
          .when(contactService).editContact(Mockito.any());

      mockMvc.perform(put(URL)
              .contentType(MediaType.APPLICATION_JSON)
              .content("{}"))
          .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("editContactValidRequests")
    void blockContactOk(String validRequest, EditContactCommand expectedCommand) throws Exception {

      mockMvc.perform(put(URL)
              .contentType(MediaType.APPLICATION_JSON)
              .content(validRequest))
          .andExpect(status().isOk());

      verify(contactService)
          .editContact(expectedCommand);
    }

    private static Stream<Arguments> editContactValidRequests() {
      return Stream.of(
          Arguments.of("""
            {
              "displayName": "new-name",
              "status": "APPROVED"
            }
          """,
              new EditContactCommand("test-user-id", "contact-user-id", "new-name", ContactStatus.APPROVED)),
          Arguments.of("""
            {
              "displayName": "new-name",
              "status": "BLOCKED"
            }
          """,
              new EditContactCommand("test-user-id", "contact-user-id", "new-name", ContactStatus.BLOCKED)),
          Arguments.of("""
            {
              "displayName": "new-name"
            }
          """,
            new EditContactCommand("test-user-id", "contact-user-id", "new-name", null)),
          Arguments.of("""
            {}
          """,
              new EditContactCommand("test-user-id", "contact-user-id", null, null))
      );
    }
  }
}
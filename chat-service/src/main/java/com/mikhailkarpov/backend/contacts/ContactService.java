package com.mikhailkarpov.backend.contacts;

import com.mikhailkarpov.backend.conversation.ConversationService;
import com.mikhailkarpov.backend.users.UserNotFoundException;
import com.mikhailkarpov.backend.users.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ContactService {

  private final ContactRepository contactRepository;
  private final ConversationService conversationService;
  private final UserService userService;

  public ContactService(
      ContactRepository contactRepository,
      ConversationService conversationService,
      UserService userService) {

    this.contactRepository = contactRepository;
    this.conversationService = conversationService;
    this.userService = userService;
  }

  @Transactional
  public void addContact(AddContactCommand command) {

    var userId = command.userId();
    var contactUserId = command.contactUserId();
    if (userId.equals(contactUserId)) {
      throw new ContactNotAllowedException();
    }
    var contact = contactRepository.findContact(userId, contactUserId).orElse(null);

    if (contact == null) {
      var user = userService.findById(userId)
          .orElseThrow(() -> new UserNotFoundException(userId));

      var contactUser = userService.findById(contactUserId)
          .orElseThrow(() -> new UserNotFoundException(contactUserId));

      var conversation = conversationService.createConversation(userId, contactUserId);

      contact = Contact.builder()
          .conversationId(conversation.getId())
          .userId(userId)
          .contactUser(contactUser)
          .status(ContactStatus.APPROVED)
          .build();

      var reverseContact = Contact.builder()
          .conversationId(conversation.getId())
          .userId(contactUserId)
          .contactUser(user)
          .status(ContactStatus.PENDING)
          .build();

      contactRepository.addContact(contact);
      contactRepository.addContact(reverseContact);
      log.debug("Contacts created: {}, {}", contact, reverseContact);
    }

    if (!contact.isApproved()) {
      contact.approve();
      contactRepository.updateContact(contact);
      conversationService.unblockParticipant(contact.getConversationId(), contact.getContactUserId());
      log.debug("Contact approved: {}", contact);
    }
  }

  @Transactional
  public void editContact(EditContactCommand command) {

    var contact = contactRepository.findContact(command.userId(), command.contactUserId())
        .orElseThrow(ContactNotFoundException::new);

    boolean needsUpdate = false;

    if (command.contactDisplayName() != null &&
        !command.contactDisplayName().equals(contact.getContactDisplayName())) {
      needsUpdate = true;
      contact.updateDisplayName(command.contactDisplayName());
    }

    if (command.contactStatus() != null &&
        command.contactStatus() != contact.getStatus()) {
      needsUpdate = true;
      if (command.contactStatus() == ContactStatus.APPROVED) {
        contact.approve();
        conversationService.unblockParticipant(contact.getConversationId(), contact.getContactUserId());
      }
      if (command.contactStatus() == ContactStatus.BLOCKED) {
        contact.block();
        conversationService.blockParticipant(contact.getConversationId(), contact.getContactUserId());
      }
    }

    if (needsUpdate) {
      contactRepository.updateContact(contact);
      log.debug("Contact updated: {}", contact);
    }
  }

  public List<Contact> listContacts(ContactListQuery query) {

    return contactRepository.findContacts(query.userId(), query.statuses());
  }

  @Transactional(readOnly = true)
  public Contact getContact(String userId, String contactUserId) {

    return contactRepository.findContact(userId, contactUserId)
        .orElseThrow(ContactNotFoundException::new);
  }
}

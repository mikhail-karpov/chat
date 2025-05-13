package com.mikhailkarpov.backend.contacts;

import com.mikhailkarpov.backend.conversation.ConversationService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ContactService {

  private final ContactRepository contactRepository;
  private final ConversationService conversationService;

  public ContactService(
      ContactRepository contactRepository, ConversationService conversationService) {

    this.contactRepository = contactRepository;
    this.conversationService = conversationService;
  }

  @Transactional
  public void addContact(AddContactCommand command) {

    var userId = command.user().id();
    var contactUserId = command.contact().id();
    var contact = contactRepository.findContact(userId, contactUserId).orElse(null);

    if (contact == null) {
      var conversation = conversationService.createConversation(userId, contactUserId);

      contact = Contact.builder()
          .conversationId(conversation.getId())
          .userId(userId)
          .contactUserId(contactUserId)
          .status(ContactStatus.APPROVED)
          .build();

      var reverseContact = Contact.builder()
          .conversationId(conversation.getId())
          .userId(contactUserId)
          .contactUserId(userId)
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
  public void blockContact(BlockContactCommand command) {

    Contact contact = contactRepository
        .findContact(command.user().id(), command.contact().id())
        .orElseThrow(ContactNotFoundException::new);

    if (contact.isApproved()) {
      contact.block();
      contactRepository.updateContact(contact);
      conversationService.blockParticipant(contact.getConversationId(), contact.getContactUserId());
      log.debug("Contact blocked: {}", contact);
    }
  }

  public List<ContactView> listContacts(ContactListQuery query) {

    return contactRepository.findContacts(query.userId(), query.statuses());
  }

}

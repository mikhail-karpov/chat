package com.mikhailkarpov.backend.contacts.web;

import com.mikhailkarpov.backend.contacts.AddContactCommand;
import com.mikhailkarpov.backend.contacts.BlockContactCommand;
import com.mikhailkarpov.backend.contacts.ContactListQuery;
import com.mikhailkarpov.backend.contacts.ContactService;
import com.mikhailkarpov.backend.contacts.ContactStatus;
import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserNotFoundException;
import com.mikhailkarpov.backend.users.UserService;
import java.util.EnumSet;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contacts")
public class ContactController {

  private final ContactService contactService;
  private final UserService userService;

  public ContactController(ContactService contactService, UserService userService) {
    this.contactService = contactService;
    this.userService = userService;
  }

  @GetMapping
  public ContactListResponse listContacts(
      @AuthenticationPrincipal User user, ContactListRequest request) {

    var statuses = CollectionUtils.isEmpty(request.statuses())
        ? EnumSet.of(ContactStatus.APPROVED)
        : EnumSet.copyOf(request.statuses());
    var contactQuery = new ContactListQuery(user.id(), statuses);
    var contacts = contactService.listContacts(contactQuery);
    return new ContactListResponse(contacts);
  }

  @PostMapping("/{contactUserId}")
  public void addContact(
      @AuthenticationPrincipal User user, @PathVariable String contactUserId) {

    var contactUser = userService.findById(contactUserId)
        .orElseThrow(() -> new UserNotFoundException(contactUserId));
    contactService.addContact(new AddContactCommand(user, contactUser));
  }

  @PostMapping("/{contactUserId}/block")
  public void blockContact(
      @AuthenticationPrincipal User user, @PathVariable String contactUserId) {

    var contact = userService.findById(contactUserId)
        .orElseThrow(() -> new UserNotFoundException(contactUserId));
    contactService.blockContact(new BlockContactCommand(user, contact));
  }
}

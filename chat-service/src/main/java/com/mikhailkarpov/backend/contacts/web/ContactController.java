package com.mikhailkarpov.backend.contacts.web;

import com.mikhailkarpov.backend.config.OpenApiSecurityScheme;
import com.mikhailkarpov.backend.contacts.AddContactCommand;
import com.mikhailkarpov.backend.contacts.ContactListQuery;
import com.mikhailkarpov.backend.contacts.ContactService;
import com.mikhailkarpov.backend.contacts.ContactStatus;
import com.mikhailkarpov.backend.contacts.EditContactCommand;
import com.mikhailkarpov.backend.users.User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.EnumSet;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contacts")
@OpenApiSecurityScheme
public class ContactController {

  private final ContactService contactService;

  public ContactController(ContactService contactService) {
    this.contactService = contactService;
  }

  @GetMapping
  public ContactListResponse listContacts(
      @AuthenticationPrincipal User user, @ParameterObject ContactListRequest request) {

    var statuses = CollectionUtils.isEmpty(request.statuses())
        ? EnumSet.of(ContactStatus.APPROVED)
        : EnumSet.copyOf(request.statuses());
    var contactQuery = new ContactListQuery(user.id(), statuses);
    var contacts = contactService.listContacts(contactQuery);
    return ContactListResponse.from(contacts);
  }

  @GetMapping("/{contactUserId}")
  public ContactResponse getContact(
      @AuthenticationPrincipal User user, @PathVariable String contactUserId) {

    var contact = contactService.getContact(user.id(), contactUserId);
    return ContactResponse.from(contact);
  }

  @PostMapping("/{contactUserId}")
  public void addContact(
      @AuthenticationPrincipal User user, @PathVariable String contactUserId) {

    contactService.addContact(new AddContactCommand(user.id(), contactUserId));
  }

  @PutMapping("/{contactUserId}")
  public void editContact(
      @AuthenticationPrincipal User user,
      @PathVariable String contactUserId,
      @Valid @RequestBody EditContactRequest request) {

    var command = new EditContactCommand(user.id(), contactUserId, request.displayName(), request.status());
    contactService.editContact(command);
  }

  @Operation(deprecated = true)
  @PostMapping("/{contactUserId}/block")
  public void blockContact(
      @AuthenticationPrincipal User user, @PathVariable String contactUserId) {

    var command = new EditContactCommand(user.id(), contactUserId, null, ContactStatus.BLOCKED);
    contactService.editContact(command);
  }
}

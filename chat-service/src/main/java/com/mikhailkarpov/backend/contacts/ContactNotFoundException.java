package com.mikhailkarpov.backend.contacts;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ContactNotFoundException extends RuntimeException {

  public ContactNotFoundException() {
    super("Contact not found");
  }

}

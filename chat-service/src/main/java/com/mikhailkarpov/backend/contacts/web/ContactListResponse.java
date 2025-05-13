package com.mikhailkarpov.backend.contacts.web;

import com.mikhailkarpov.backend.contacts.ContactView;
import java.util.List;

public record ContactListResponse(List<ContactView> contacts) {

}
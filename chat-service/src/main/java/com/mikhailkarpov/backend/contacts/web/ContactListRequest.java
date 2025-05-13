package com.mikhailkarpov.backend.contacts.web;

import com.mikhailkarpov.backend.contacts.ContactStatus;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ContactListRequest(List<@NotNull ContactStatus> statuses) {

}

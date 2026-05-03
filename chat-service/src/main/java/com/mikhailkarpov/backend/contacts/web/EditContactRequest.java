package com.mikhailkarpov.backend.contacts.web;

import com.mikhailkarpov.backend.contacts.ContactStatus;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;

public record EditContactRequest(
    @Nullable @Size(min = 3, max = 32) String displayName,
    @Nullable ContactStatus status) {
}

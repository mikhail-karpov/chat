package com.mikhailkarpov.backend.contacts;

import org.jspecify.annotations.Nullable;

public record EditContactCommand(
    String userId,
    String contactUserId,
    @Nullable String contactDisplayName,
    @Nullable ContactStatus contactStatus) {
}

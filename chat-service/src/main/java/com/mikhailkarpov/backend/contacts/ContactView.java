package com.mikhailkarpov.backend.contacts;

import java.util.UUID;

public record ContactView(UUID conversationId, String id, String username, String displayName, ContactStatus status) {

}

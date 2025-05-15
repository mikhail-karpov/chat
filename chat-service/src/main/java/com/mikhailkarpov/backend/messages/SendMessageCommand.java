package com.mikhailkarpov.backend.messages;

import java.util.UUID;

public record SendMessageCommand(String userId, UUID conversationId, String text) {

}

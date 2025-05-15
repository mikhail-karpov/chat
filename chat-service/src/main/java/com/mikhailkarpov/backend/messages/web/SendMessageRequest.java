package com.mikhailkarpov.backend.messages.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record SendMessageRequest(

    @NotNull
    UUID conversationId,

    @NotBlank
    @Size(min = 3, max = 128)
    String text

) {

}

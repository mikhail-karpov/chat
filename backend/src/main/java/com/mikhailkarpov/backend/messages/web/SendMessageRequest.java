package com.mikhailkarpov.backend.messages.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendMessageRequest(

    @NotBlank
    @Size(min = 3, max = 128)
    String text

) {

}

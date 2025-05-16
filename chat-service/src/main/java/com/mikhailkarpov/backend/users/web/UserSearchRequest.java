package com.mikhailkarpov.backend.users.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserSearchRequest(

    @NotBlank
    @Size(min = 3, max = 32)
    String query) {

}

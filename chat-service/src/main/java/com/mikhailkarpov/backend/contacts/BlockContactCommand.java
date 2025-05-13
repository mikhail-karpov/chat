package com.mikhailkarpov.backend.contacts;

import com.mikhailkarpov.backend.users.User;

public record BlockContactCommand(User user, User contact) {

}

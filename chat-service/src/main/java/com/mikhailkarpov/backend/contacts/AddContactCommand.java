package com.mikhailkarpov.backend.contacts;

import com.mikhailkarpov.backend.users.User;

public record AddContactCommand(User user, User contact) {

}

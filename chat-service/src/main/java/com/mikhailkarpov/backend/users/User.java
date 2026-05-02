package com.mikhailkarpov.backend.users;

import java.io.Serializable;

public record User(String id, String username, String displayName) implements Serializable {

}

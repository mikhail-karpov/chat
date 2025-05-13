package com.mikhailkarpov.backend.contacts;

import java.util.EnumSet;

public record ContactListQuery(String userId, EnumSet<ContactStatus> statuses) {

}

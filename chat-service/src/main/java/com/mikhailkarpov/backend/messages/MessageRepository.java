package com.mikhailkarpov.backend.messages;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {

  void addMessage(Message message);

  List<Message> listMessages(UUID conversationId, int limit);

}

package com.mikhailkarpov.backend.messages;

import java.util.List;

public interface MessageRepository {

  void addMessage(Message message);

  List<Message> listMessages(int limit);

}

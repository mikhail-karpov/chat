package com.mikhailkarpov.backend.messages;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

  private final MessageRepository messageRepository;

  public MessageService(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  public Message createMessage(String userId, String text) {
    Message message = new Message(userId, text);
    messageRepository.addMessage(message);
    return message;
  }

  public List<Message> listMessages(int limit) {
    return messageRepository.listMessages(limit);
  }

}

package com.mikhailkarpov.backend.messages;

import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

  private final MessageRepository messageRepository;
  private final ApplicationEventPublisher eventPublisher;

  public MessageService(
      MessageRepository messageRepository, ApplicationEventPublisher eventPublisher) {

    this.messageRepository = messageRepository;
    this.eventPublisher = eventPublisher;
  }

  public Message createMessage(String userId, String text) {
    Message message = new Message(userId, text);
    messageRepository.addMessage(message);
    eventPublisher.publishEvent(message);
    return message;
  }

  public List<Message> listMessages(int limit) {
    return messageRepository.listMessages(limit);
  }

}

package com.mikhailkarpov.backend.messages;

import com.mikhailkarpov.backend.conversation.ConversationParticipantNotFoundException;
import com.mikhailkarpov.backend.conversation.ConversationService;
import java.util.List;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

  private final ConversationService conversationService;
  private final MessageRepository messageRepository;
  private final ApplicationEventPublisher eventPublisher;

  public MessageService(
      ConversationService conversationService,
      MessageRepository messageRepository,
      ApplicationEventPublisher eventPublisher) {

    this.conversationService = conversationService;
    this.messageRepository = messageRepository;
    this.eventPublisher = eventPublisher;
  }

  @Transactional
  public Message sendMessage(SendMessageCommand command) {

    var participant = conversationService.findParticipant(command.conversationId(), command.userId());
    if (participant.isBlocked()) {
      throw new ConversationParticipantNotFoundException();
    }

    Message message = Message.builder()
        .conversationId(command.conversationId())
        .userId(command.userId())
        .text(command.text())
        .build();

    messageRepository.addMessage(message);
    eventPublisher.publishEvent(message);
    return message;
  }

  @Transactional(readOnly = true)
  public List<Message> listMessages(UUID conversationId, int limit) {
    return messageRepository.listMessages(conversationId, limit);
  }

}

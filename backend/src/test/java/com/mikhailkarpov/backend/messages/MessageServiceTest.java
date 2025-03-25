package com.mikhailkarpov.backend.messages;

import com.mikhailkarpov.backend.messages.memory.InMemoryMessageRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@RecordApplicationEvents
class MessageServiceTest {

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Autowired
  private ApplicationEvents applicationEvents;

  private MessageService messageService;

  @BeforeEach
  void setUp() {

    InMemoryMessageRepository messageRepository = new InMemoryMessageRepository();
    this.messageService = new MessageService(messageRepository, this.eventPublisher);
  }

  @Test
  void addMessage() {

    Message message1 = messageService.createMessage("user-1", "test message 1");
    Message message2 = messageService.createMessage("user-2", "test message 2");

    List<Message> messages = messageService.listMessages(10);
    Assertions.assertThat(messages).containsExactly(message2, message1);

    List<Message> events = applicationEvents.stream(Message.class).toList();
    Assertions.assertThat(events).containsExactly(message1, message2);
  }

  @Test
  void listMessagesEmpty() {

    Assertions.assertThat(messageService.listMessages(10)).isEmpty();
  }
}
package com.mikhailkarpov.backend.messages;

import com.mikhailkarpov.backend.config.IntegrationTest;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@IntegrationTest
@RecordApplicationEvents
class MessageServiceTest {

  @Autowired
  private ApplicationEvents applicationEvents;

  @Autowired
  private MessageService messageService;

  @Test
  void addMessage() {

    Message message1 = messageService.createMessage("user-1", "test message 1");
    Message message2 = messageService.createMessage("user-2", "test message 2");

    List<Message> messages = messageService.listMessages(10);
    Assertions.assertThat(messages).containsExactly(message2, message1);

    List<Message> events = applicationEvents.stream(Message.class).toList();
    Assertions.assertThat(events).containsExactly(message1, message2);
  }

}
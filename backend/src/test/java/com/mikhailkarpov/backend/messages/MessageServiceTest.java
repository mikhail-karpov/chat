package com.mikhailkarpov.backend.messages;

import com.mikhailkarpov.backend.messages.memory.InMemoryMessageRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MessageServiceTest {

  private final MessageService messageService = new MessageService(new InMemoryMessageRepository());

  @Test
  void addMessage() {

    Message message1 = messageService.createMessage("user-1", "test message 1");
    Message message2 = messageService.createMessage("user-2", "test message 2");
    List<Message> messages = messageService.listMessages(10);

    Assertions.assertThat(messages).containsExactly(message2, message1);
  }

  @Test
  void listMessagesEmpty() {

    Assertions.assertThat(messageService.listMessages(10)).isEmpty();
  }
}
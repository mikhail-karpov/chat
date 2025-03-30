package com.mikhailkarpov.backend.messages;

import com.mikhailkarpov.backend.config.IntegrationTest;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Test
  void addAndFindAll() {

    for (int i = 1; i <= 10; i++) {
      String userId = "user-" + i;
      String text = "message-" + i;
      messageRepository.addMessage(new Message(userId, text));
      try {
        Thread.sleep(100L);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }

    List<Message> messages = messageRepository.listMessages(5);

    Assertions.assertThat(messages)
        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "createdAt")
        .containsExactly(
            new Message("user-10", "message-10"),
            new Message("user-9", "message-9"),
            new Message("user-8", "message-8"),
            new Message("user-7", "message-7"),
            new Message("user-6", "message-6")
        );
  }
}
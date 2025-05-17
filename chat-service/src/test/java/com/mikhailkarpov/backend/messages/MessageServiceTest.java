package com.mikhailkarpov.backend.messages;

import static org.assertj.core.api.Assertions.*;

import com.mikhailkarpov.backend.config.IntegrationTest;
import com.mikhailkarpov.backend.conversation.ConversationParticipantNotFoundException;
import com.mikhailkarpov.backend.conversation.ConversationService;
import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@RecordApplicationEvents
@Transactional
@Rollback
class MessageServiceTest {

  private final User user1 = new User("user-1", "user-1-username");
  private final User user2 = new User("user-2", "user-2-username");

  @Autowired
  private ApplicationEvents events;

  @Autowired
  private MessageService messageService;

  @Autowired
  private UserService userService;

  @Autowired
  private ConversationService conversationService;


  @BeforeEach
  void setUp() {
      userService.save(user1);
      userService.save(user2);
  }


  @Nested
  class AddMessageTests {

    @Test
    void addMessage() {

      var conversationId = conversationService.createConversation("user-1", "user-2").getId();

      var message1 = messageService.sendMessage(
          new SendMessageCommand("user-1", conversationId, "test message 1"));

      var message2 = messageService.sendMessage(
          new SendMessageCommand("user-2", conversationId, "test message 2"));

      assertThat(messageService.listMessages(conversationId, 10))
          .containsExactlyInAnyOrder(message2, message1);

      assertThat(events.stream(Message.class))
          .containsExactly(message1, message2);
    }

    @Test
    void addMessageNoConversation() {

      var command = new SendMessageCommand("user-1", UUID.randomUUID(), "test message");

      assertThatThrownBy(() -> messageService.sendMessage(command))
          .isInstanceOf(ConversationParticipantNotFoundException.class);

      assertThat(events.stream(Message.class))
          .isEmpty();
    }

    @Test
    void addMessageNoConversationParticipant() {

      var conversationId = conversationService.createConversation("user-1").getId();
      var command = new SendMessageCommand("user-2", conversationId, "test message");

      assertThatThrownBy(() -> messageService.sendMessage(command))
          .isInstanceOf(ConversationParticipantNotFoundException.class);

      assertThat(events.stream(Message.class))
          .isEmpty();
    }

    @Test
    void addMessageParticipantBlocked() {

      var conversationId = conversationService.createConversation("user-1").getId();
      conversationService.blockParticipant(conversationId, "user-1");
      var command = new SendMessageCommand("user-1", conversationId, "test message");

      assertThatThrownBy(() -> messageService.sendMessage(command))
          .isInstanceOf(ConversationParticipantNotFoundException.class);

      assertThat(events.stream(Message.class))
          .isEmpty();
    }

  }

}
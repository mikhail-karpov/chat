package com.mikhailkarpov.backend.conversation;

import static org.assertj.core.api.Assertions.*;

import com.mikhailkarpov.backend.config.IntegrationTest;
import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserService;
import java.util.UUID;
import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@Transactional
@Rollback
class ConversationServiceTest {

  private final User user1 = new User("user-1", "username-1");
  private final User user2 = new User("user-2", "username-2");

  @Autowired
  private ConversationService conversationService;

  @Autowired
  private UserService userService;


  @BeforeEach
  void setUp() {
    userService.save(user1);
    userService.save(user2);
  }


  @Nested
  class CreateConversationTests {

    @Test
    void createConversation() {

      var conversation = conversationService.createConversation(user1.id(), user2.id());
      var conversationId = conversation.getId();

      assertThat(conversationService.findParticipants(conversationId)).hasSize(2);
      assertThat(conversationService.findParticipant(conversationId, user1.id())).isNotNull();
      assertThat(conversationService.findParticipant(conversationId, user2.id())).isNotNull();
    }
  }


  @Nested
  class BlockParticipantTests {

    @Test
    void blockParticipant() {

      var conversation = conversationService.createConversation(user1.id(), user2.id());
      var conversationId = conversation.getId();
      conversationService.blockParticipant(conversationId, user1.id());

      assertThat(conversationService.findParticipants(conversationId)).hasSize(2);
      assertThat(conversationService.findParticipant(conversationId, user1.id()))
          .matches(ConversationParticipant::isBlocked)
          .isNotNull();
    }

    @Test
    void blockParticipantUserFound() {

      assertThatThrownBy(() -> conversationService.blockParticipant(UUID.randomUUID(), "not-found"))
          .isInstanceOf(ConversationParticipantNotFoundException.class);
    }

    @Test
    void blockParticipantNotMember() {

      var conversation = conversationService.createConversation(user1.id());
      var conversationId = conversation.getId();

      assertThatThrownBy(() -> conversationService.blockParticipant(conversationId, user2.id()))
          .isInstanceOf(ConversationParticipantNotFoundException.class);
    }
  }

  @Nested
  class UnlockParticipantTests {

    @Test
    void unblockParticipant() {

      var conversation = conversationService.createConversation(user1.id(), user2.id());
      var conversationId = conversation.getId();
      conversationService.blockParticipant(conversationId, user1.id());
      conversationService.unblockParticipant(conversationId, user1.id());

      assertThat(conversationService.findParticipants(conversationId)).hasSize(2);
      assertThat(conversationService.findParticipant(conversationId, user1.id()))
          .matches(Predicate.not(ConversationParticipant::isBlocked))
          .isNotNull();
    }

    @Test
    void unblockParticipantUserFound() {

      assertThatThrownBy(() -> conversationService.unblockParticipant(UUID.randomUUID(), "not-found"))
          .isInstanceOf(ConversationParticipantNotFoundException.class);
    }

    @Test
    void unblockParticipantNotMember() {

      var conversation = conversationService.createConversation(user1.id());
      var conversationId = conversation.getId();

      assertThatThrownBy(() -> conversationService.unblockParticipant(conversationId, user2.id()))
          .isInstanceOf(ConversationParticipantNotFoundException.class);
    }
  }

}
package com.mikhailkarpov.backend.messages;

import com.mikhailkarpov.backend.config.IntegrationTest;
import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserService;
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

  @Autowired
  private UserService userService;

  @Test
  void addMessage() {

    userService.save(new User("user-1", "user-1-username"));
    userService.save(new User("user-2", "user-2-username"));

    Message message1 = messageService.createMessage("user-1", "test message 1");
    Message message2 = messageService.createMessage("user-2", "test message 2");

    List<Message> messages = messageService.listMessages(10);
    Assertions.assertThat(messages).containsExactly(message2, message1);

    List<Message> events = applicationEvents.stream(Message.class).toList();
    Assertions.assertThat(events).containsExactly(message1, message2);
  }

}
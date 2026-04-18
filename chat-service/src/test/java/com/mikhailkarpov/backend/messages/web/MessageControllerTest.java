package com.mikhailkarpov.backend.messages.web;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mikhailkarpov.backend.config.SecurityTestConfig;
import com.mikhailkarpov.backend.config.WithMockChatUser;
import com.mikhailkarpov.backend.messages.Message;
import com.mikhailkarpov.backend.messages.MessageService;
import com.mikhailkarpov.backend.messages.SendMessageCommand;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MessageController.class)
@Import(SecurityTestConfig.class)
@WithMockChatUser
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private MessageService messageService;


  @Nested
  class PostMessageTests {

    @Test
    void postMessageOk() throws Exception {

      var conversationId = UUID.fromString("9862f745-03e0-4a67-83c2-3e722e9d8ef3");
      var command = new SendMessageCommand("test-user-id", conversationId, "test message");
      var message = Message.builder()
          .conversationId(conversationId)
          .userId("test-user-id")
          .text("test message")
          .build();

      when(messageService.sendMessage(command)).thenReturn(message);

      mockMvc.perform(post("/api/v1/messages")
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                  {
                    "conversationId": "9862f745-03e0-4a67-83c2-3e722e9d8ef3",
                    "text": "test message"
                  }
                  """))
          .andExpect(status().isOk())
          .andExpect(jsonPath("id").isNotEmpty())
          .andExpect(jsonPath("conversationId").value("9862f745-03e0-4a67-83c2-3e722e9d8ef3"))
          .andExpect(jsonPath("userId").value("test-user-id"))
          .andExpect(jsonPath("text").value("test message"))
          .andExpect(jsonPath("createdAt").isNotEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        """
            {"text": "test message", "conversationId": "string"}
            """,
        """
            {"text": "test message", "conversationId": null},
            """,
        """
            {"text": "test message", "conversationId": ""}
            """
    })
    void postMessageInvalidConversation(String illegalContent) throws Exception {

      mockMvc.perform(post("/api/v1/messages")
              .contentType(MediaType.APPLICATION_JSON)
              .content(illegalContent))
          .andExpect(status().isBadRequest());

      verifyNoInteractions(messageService);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        """
            {"conversationId": "9862f745-03e0-4a67-83c2-3e722e9d8ef3", "text": ""}
            """,
        """
            {"conversationId": "9862f745-03e0-4a67-83c2-3e722e9d8ef3", "text": null},
            """,
        """
            {"conversationId": "9862f745-03e0-4a67-83c2-3e722e9d8ef3", "text": "ab"}
            """
    })
    void postMessageInvalidText(String illegalContent) throws Exception {

      mockMvc.perform(post("/api/v1/messages")
              .contentType(MediaType.APPLICATION_JSON)
              .content(illegalContent))
          .andExpect(status().isBadRequest());

      verifyNoInteractions(messageService);
    }
  }


  @Nested
  class ListMessagesTests {

    private final List<Message> messages = List.of(
        Message.builder().conversationId(UUID.randomUUID())
            .userId("user-1")
            .text("message 1").build(),
        Message.builder().conversationId(UUID.randomUUID())
            .userId("user-2")
            .text("message 2").build()
    );

    @Test
    void listMessagesLimitOk() throws Exception {

      var conversationId = "9862f745-03e0-4a67-83c2-3e722e9d8ef3";

      when(messageService.listMessages(UUID.fromString(conversationId), 15))
          .thenReturn(messages);

      mockMvc.perform(get("/api/v1/messages?conversationId=9862f745-03e0-4a67-83c2-3e722e9d8ef3&limit=15"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("[0].id").isNotEmpty())
          .andExpect(jsonPath("[0].conversationId").isNotEmpty())
          .andExpect(jsonPath("[0].userId").value("user-1"))
          .andExpect(jsonPath("[0].text").value("message 1"))
          .andExpect(jsonPath("[0].createdAt").isNotEmpty())
          .andExpect(jsonPath("[1].id").isNotEmpty())
          .andExpect(jsonPath("[1].conversationId").isNotEmpty())
          .andExpect(jsonPath("[1].userId").value("user-2"))
          .andExpect(jsonPath("[1].text").value("message 2"))
          .andExpect(jsonPath("[1].createdAt").isNotEmpty());
    }

    @Test
    void listMessagesOk() throws Exception {

      var conversationId = "9862f745-03e0-4a67-83c2-3e722e9d8ef3";
      when(messageService.listMessages(UUID.fromString(conversationId), 10))
          .thenReturn(messages);

      mockMvc.perform(get("/api/v1/messages?conversationId=9862f745-03e0-4a67-83c2-3e722e9d8ef3"))
          .andExpect(status().isOk());
    }

    @Test
    void listMessagesBadRequest() throws Exception {

      mockMvc.perform(get("/api/v1/messages"))
          .andExpect(status().isBadRequest());

      verifyNoInteractions(messageService);
    }
  }
}
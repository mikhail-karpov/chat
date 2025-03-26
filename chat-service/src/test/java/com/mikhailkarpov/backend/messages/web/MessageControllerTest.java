package com.mikhailkarpov.backend.messages.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mikhailkarpov.backend.config.SecurityTestConfig;
import com.mikhailkarpov.backend.config.WithMockChatUser;
import com.mikhailkarpov.backend.messages.Message;
import com.mikhailkarpov.backend.messages.MessageService;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

      Message message = new Message("test-user-id", "test message");

      when(messageService.createMessage("test-user-id", "test message"))
          .thenReturn(message);

      mockMvc.perform(post("/api/v1/messages")
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                  {"text": "test message"}
                  """))
          .andExpect(status().isOk())
          .andExpect(jsonPath("id").value(message.id()))
          .andExpect(jsonPath("userId").value("test-user-id"))
          .andExpect(jsonPath("text").value("test message"))
          .andExpect(jsonPath("createdAt").isNotEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        """
            {"text": ""}
            """,
        """
            {"text": null},
            """,
        """
            {"text": "ab"}
            """
    })
    void postMessageBadRequest(String illegalContent) throws Exception {

      mockMvc.perform(post("/api/v1/messages")
              .contentType(MediaType.APPLICATION_JSON)
              .content(illegalContent))
          .andExpect(status().isBadRequest());
    }
  }


  @Nested
  class ListMessagesTests {

    private final List<Message> messages = List.of(
        new Message("user-1", "message 1"),
        new Message("user-2", "message 2")
    );

    @Test
    void listMessagesLimitOk() throws Exception {

      when(messageService.listMessages(15)).thenReturn(messages);

      mockMvc.perform(get("/api/v1/messages?limit=15"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("[0].id").isNotEmpty())
          .andExpect(jsonPath("[0].userId").value("user-1"))
          .andExpect(jsonPath("[0].text").value("message 1"))
          .andExpect(jsonPath("[0].createdAt").isNotEmpty())
          .andExpect(jsonPath("[1].id").isNotEmpty())
          .andExpect(jsonPath("[1].userId").value("user-2"))
          .andExpect(jsonPath("[1].text").value("message 2"))
          .andExpect(jsonPath("[1].createdAt").isNotEmpty());
    }

    @Test
    void listMessagesOk() throws Exception {

      when(messageService.listMessages(10)).thenReturn(messages);

      mockMvc.perform(get("/api/v1/messages"))
          .andExpect(status().isOk());
    }
  }
}
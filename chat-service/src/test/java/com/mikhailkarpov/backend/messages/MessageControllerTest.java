package com.mikhailkarpov.backend.messages;

import com.mikhailkarpov.backend.config.SecurityTestConfig;
import com.mikhailkarpov.backend.messages.web.MessageController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MessageController.class)
@Import(SecurityTestConfig.class)
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;


}
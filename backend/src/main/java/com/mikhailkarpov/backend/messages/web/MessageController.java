package com.mikhailkarpov.backend.messages.web;

import com.mikhailkarpov.backend.messages.Message;
import com.mikhailkarpov.backend.messages.MessageService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @PostMapping
  public Message sendMessage(
      Authentication authentication, @Valid @RequestBody SendMessageRequest request) {

    return messageService.createMessage(authentication.getName(), request.text());
  }

  @GetMapping
  public List<Message> listMessages(@RequestParam(defaultValue = "10", required = false) int limit) {

    return messageService.listMessages(limit);
  }

}

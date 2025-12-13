package com.mikhailkarpov.backend.messages.web;

import com.mikhailkarpov.backend.config.OpenApiSecurityScheme;
import com.mikhailkarpov.backend.messages.Message;
import com.mikhailkarpov.backend.messages.MessageService;
import com.mikhailkarpov.backend.messages.SendMessageCommand;
import com.mikhailkarpov.backend.users.User;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
@OpenApiSecurityScheme
public class MessageController {

  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @PostMapping
  public Message sendMessage(
      @AuthenticationPrincipal User user, @Valid @RequestBody SendMessageRequest request) {

    var command = new SendMessageCommand(user.id(), request.conversationId(), request.text());
    return messageService.sendMessage(command);
  }

  @GetMapping
  public List<Message> listMessages(
      @RequestParam(value = "conversationId") UUID conversationId,
      @RequestParam(defaultValue = "10", required = false) int limit) {

    return messageService.listMessages(conversationId, limit);
  }

}

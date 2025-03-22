package com.mikhailkarpov.backend.messages;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

  private final List<Message> messages = new CopyOnWriteArrayList<>();

  @PostConstruct
  private void init() {
    IntStream.range(0, 21)
        .boxed()
        .map("Message %d"::formatted)
        .map(Message::new)
        .forEach(messages::add);
  }

  @PostMapping
  public Message sendMessage(@Valid @RequestBody SendMessageRequest request) {
    Message message = new Message(request.text());
    messages.add(message);
    return message;
  }

  @GetMapping
  public List<Message> listMessages(@RequestParam(defaultValue = "10", required = false) int limit) {

    return messages.stream()
        .sorted(Comparator.comparing(Message::createdAt).reversed())
        .limit(limit)
        .toList();
  }

}

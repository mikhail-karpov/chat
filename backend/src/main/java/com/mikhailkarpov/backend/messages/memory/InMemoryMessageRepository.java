package com.mikhailkarpov.backend.messages.memory;

import com.mikhailkarpov.backend.messages.Message;
import com.mikhailkarpov.backend.messages.MessageRepository;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryMessageRepository implements MessageRepository {

  private final List<Message> messages;

  public InMemoryMessageRepository() {
    this(new CopyOnWriteArrayList<>());
  }

  public InMemoryMessageRepository(List<Message> messages) {
    this.messages = messages;
  }

  @Override
  public void addMessage(Message message) {
    messages.add(message);
  }

  @Override
  public List<Message> listMessages(int limit) {
    return messages.stream()
        .sorted(Comparator.comparing(Message::createdAt).reversed())
        .limit(limit)
        .toList();
  }
}

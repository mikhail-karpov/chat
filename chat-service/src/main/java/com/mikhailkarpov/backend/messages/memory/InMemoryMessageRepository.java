package com.mikhailkarpov.backend.messages.memory;

import com.mikhailkarpov.backend.messages.Message;
import com.mikhailkarpov.backend.messages.MessageRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryMessageRepository implements MessageRepository {

  private final Map<UUID, List<Message>> messages;

  public InMemoryMessageRepository() {
    this(new ConcurrentHashMap<>());
  }

  public InMemoryMessageRepository(Map<UUID, List<Message>> messages) {
    this.messages = messages;
  }

  @Override
  public void addMessage(Message message) {

    var messagesByConversation = getMessages(message.getConversationId());
    messagesByConversation.add(message);
  }

  @Override
  public List<Message> listMessages(UUID conversationId, int limit) {
    return getMessages(conversationId)
        .stream()
        .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
        .limit(limit)
        .toList();
  }

  private List<Message> getMessages(UUID conversationId) {
    return messages.computeIfAbsent(conversationId, k -> new CopyOnWriteArrayList<>());
  }
}

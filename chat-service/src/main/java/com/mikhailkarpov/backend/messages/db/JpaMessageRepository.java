package com.mikhailkarpov.backend.messages.db;

import com.mikhailkarpov.backend.messages.Message;
import com.mikhailkarpov.backend.messages.MessageRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class JpaMessageRepository implements MessageRepository {

  private final EntityManager entityManager;

  public JpaMessageRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public void addMessage(Message message) {
    entityManager.persist(MessageEntity.from(message));
  }

  @Override
  public List<Message> listMessages(int limit) {
    String query = """
        SELECT new com.mikhailkarpov.backend.messages.Message(
          m.id,
          m.userId,
          m.text,
          m.createdAt
        )
        FROM MessageEntity m
        ORDER BY m.createdAt DESC
        """;

    return entityManager.createQuery(query, Message.class)
        .setMaxResults(limit)
        .getResultList();
  }
}

package com.mikhailkarpov.backend.conversation.db;

import com.mikhailkarpov.backend.conversation.Conversation;
import com.mikhailkarpov.backend.conversation.ConversationParticipant;
import com.mikhailkarpov.backend.conversation.ConversationParticipantNotFoundException;
import com.mikhailkarpov.backend.conversation.ConversationRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcConversationRepository implements ConversationRepository {

  private final JdbcClient jdbcClient;

  public JdbcConversationRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }


  @Override
  public Conversation addConversation(Conversation conversation) {

    jdbcClient.sql("""
        INSERT INTO conversation (id, is_public, is_group)
        VALUES (:id, :isPublic, :isGroup)
        """)
        .param("id", conversation.getId())
        .param("isPublic", conversation.isPublic())
        .param("isGroup", conversation.isGroup())
        .update();

    return conversation;
  }

  @Override
  public ConversationParticipant addParticipant(ConversationParticipant participant) {

    jdbcClient.sql("""
        INSERT INTO conversation_participant (conversation_id, user_id, is_blocked)
        VALUES (:conversationId, :userId, :isBlocked)
        """)
        .param("conversationId", participant.getConversationId())
        .param("userId", participant.getUserId())
        .param("isBlocked", participant.isBlocked())
        .update();

    return participant;
  }

  @Override
  public ConversationParticipant findParticipant(UUID conversationId, String userId) {

    return jdbcClient.sql("""
        SELECT is_blocked
        FROM conversation_participant
        WHERE conversation_id = :conversationId
        AND user_id = :userId
        """)
        .param("conversationId", conversationId)
        .param("userId", userId)
        .query((rs, rn) -> new ConversationParticipant(
            conversationId,
            userId,
            rs.getBoolean("is_blocked")
        ))
        .optional()
        .orElseThrow(ConversationParticipantNotFoundException::new);
  }

  @Override
  public List<ConversationParticipant> findParticipants(UUID conversationId) {

    return jdbcClient.sql("""
        SELECT user_id, is_blocked
        FROM conversation_participant
        WHERE conversation_id = :conversationId
        """)
        .param("conversationId", conversationId)
        .query((rs, rn) -> new ConversationParticipant(
            conversationId,
            rs.getString("user_id"),
            rs.getBoolean("is_blocked")
        ))
        .list();
  }

  @Override
  public void blockParticipant(UUID conversationId, String userId) {

    int updatedRows = jdbcClient.sql("""
        UPDATE conversation_participant
        SET is_blocked = true
        WHERE conversation_id = :conversationId
        AND user_id = :userId
        """)
        .param("conversationId", conversationId)
        .param("userId", userId)
        .update();

    if (updatedRows != 1) {
      throw new ConversationParticipantNotFoundException();
    }
  }

  @Override
  public void unblockParticipant(UUID conversationId, String userId) {

    int updatedRows = jdbcClient.sql("""
        UPDATE conversation_participant
        SET is_blocked = false
        WHERE conversation_id = :conversationId
        AND user_id = :userId
        """)
        .param("conversationId", conversationId)
        .param("userId", userId)
        .update();

    if (updatedRows != 1) {
      throw new ConversationParticipantNotFoundException();
    }
  }
}

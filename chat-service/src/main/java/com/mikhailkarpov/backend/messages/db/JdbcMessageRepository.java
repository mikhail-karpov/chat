package com.mikhailkarpov.backend.messages.db;

import com.mikhailkarpov.backend.messages.Message;
import com.mikhailkarpov.backend.messages.MessageRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcMessageRepository implements MessageRepository {

  private final JdbcClient jdbcClient;
  private final RowMapper<Message> messageRowMapper = new MessageRowMapper();

  public JdbcMessageRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Override
  public void addMessage(Message message) {

    jdbcClient.sql("""
            INSERT INTO message (id, conversation_id, user_id, text, created_at)
            VALUES (:id, :conversationId, :userId, :text, :createdAt)""")
        .param("id", message.getId())
        .param("conversationId", message.getConversationId())
        .param("userId", message.getUserId())
        .param("text", message.getText())
        .param("createdAt", Timestamp.from(message.getCreatedAt()))
        .update();
  }

  @Override
  public List<Message> listMessages(UUID conversationId, int limit) {

    return jdbcClient.sql("""
            SELECT
                m.id,
                m.conversation_id,
                m.user_id,
                m.text,
                m.created_at
            FROM message m
            WHERE m.conversation_id = :conversationId
            ORDER BY m.created_at DESC
            LIMIT :limit
            """)
        .param("conversationId", conversationId)
        .param("limit", limit)
        .query(messageRowMapper)
        .list();
  }

  private static class MessageRowMapper implements RowMapper<Message> {

    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {

      return Message.builder()
          .id(rs.getObject("id", UUID.class))
          .conversationId(rs.getObject("conversation_id", UUID.class))
          .userId(rs.getString("user_id"))
          .text(rs.getString("text"))
          .createdAt(rs.getTimestamp("created_at").toInstant())
          .build();
    }
  }
}
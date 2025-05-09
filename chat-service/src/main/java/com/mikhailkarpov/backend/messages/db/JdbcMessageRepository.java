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
            INSERT INTO message (id, user_id, text, created_at)
            VALUES (:id, :userId, :text, :createdAt)""")
        .param("id", message.id())
        .param("userId", message.userId())
        .param("text", message.text())
        .param("createdAt", Timestamp.from(message.createdAt()))
        .update();
  }

  @Override
  public List<Message> listMessages(int limit) {

    return jdbcClient.sql("""
            SELECT
                m.id,
                m.user_id,
                m.text,
                m.created_at
            FROM message m
            ORDER BY m.created_at DESC
            LIMIT :limit
            """)
        .param("limit", limit)
        .query(messageRowMapper)
        .list();
  }

  private static class MessageRowMapper implements RowMapper<Message> {

    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new Message(
          UUID.fromString(rs.getString("id")),
          rs.getString("user_id"),
          rs.getString("text"),
          rs.getTimestamp("created_at").toInstant()
      );
    }
  }
}
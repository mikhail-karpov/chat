package com.mikhailkarpov.backend.contacts.db;

import com.mikhailkarpov.backend.contacts.Contact;
import com.mikhailkarpov.backend.contacts.ContactNotFoundException;
import com.mikhailkarpov.backend.contacts.ContactRepository;
import com.mikhailkarpov.backend.contacts.ContactStatus;
import com.mikhailkarpov.backend.users.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcContactRepository implements ContactRepository {

  private final JdbcClient jdbcClient;
  private final RowMapper<Contact> contactRowMapper = new ContactRowMapper();

  public JdbcContactRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Override
  public void addContact(Contact contact) {

    jdbcClient.sql("""
            INSERT INTO contact (conversation_id, user_id, contact_user_id, contact_username, contact_display_name, status)
            VALUES (:conversationId, :userId, :contactUserId, :contactUsername, :contactDisplayName, :status)
            """)
        .param("conversationId", contact.getConversationId())
        .param("userId", contact.getUserId())
        .param("contactUserId", contact.getContactUserId())
        .param("contactUsername", contact.getContactUsername())
        .param("contactDisplayName", contact.getContactDisplayName())
        .param("status", contact.getStatus().name())
        .update();
  }

  @Override
  public void updateContact(Contact contact) {

    int rowsUpdated = jdbcClient.sql("""
            UPDATE contact
            SET status = :status, contact_display_name = :contactDisplayName
            WHERE user_id = :userId AND contact_user_id = :contactUserId
            """)
        .param("userId", contact.getUserId())
        .param("contactUserId", contact.getContactUserId())
        .param("contactDisplayName", contact.getContactDisplayName())
        .param("status", contact.getStatus().name())
        .update();

    if (rowsUpdated != 1) {
      throw new ContactNotFoundException();
    }
  }

  @Override
  public Optional<Contact> findContact(String userId, String contactUserId) {

    return jdbcClient.sql("""
            SELECT conversation_id, user_id, contact_user_id, contact_username, contact_display_name, status
            FROM contact
            WHERE user_id = :userId AND contact_user_id = :contactUserId
            """)
        .param("userId", userId)
        .param("contactUserId", contactUserId)
        .query(contactRowMapper)
        .optional();
  }

  @Override
  public List<Contact> findContacts(String userId, Iterable<ContactStatus> statuses) {

    Set<String> contactStatuses = new HashSet<>();
    statuses.forEach(s -> contactStatuses.add(s.name()));

    return jdbcClient.sql("""
        SELECT conversation_id, user_id, contact_user_id, contact_username, contact_display_name, status
        FROM contact c
        JOIN users u
        ON c.contact_user_id = u.id
        WHERE c.user_id = :userId AND c.status IN (:statuses)
        """)
        .param("userId", userId)
        .param("statuses", contactStatuses)
        .query(contactRowMapper)
        .list();
  }

  private static class ContactRowMapper implements RowMapper<Contact> {

    @Override
    public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new Contact(
          UUID.fromString(rs.getString("conversation_id")),
          rs.getString("user_id"),
          new User(
              rs.getString("contact_user_id"),
              rs.getString("contact_username"),
              rs.getString("contact_display_name")
          ),
          ContactStatus.valueOf(rs.getString("status"))
      );
    }
  }
}

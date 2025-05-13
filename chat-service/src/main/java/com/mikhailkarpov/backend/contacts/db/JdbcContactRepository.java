package com.mikhailkarpov.backend.contacts.db;

import com.mikhailkarpov.backend.contacts.Contact;
import com.mikhailkarpov.backend.contacts.ContactNotFoundException;
import com.mikhailkarpov.backend.contacts.ContactRepository;
import com.mikhailkarpov.backend.contacts.ContactStatus;
import com.mikhailkarpov.backend.contacts.ContactView;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcContactRepository implements ContactRepository {

  private final JdbcClient jdbcClient;

  public JdbcContactRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Override
  public void addContact(Contact contact) {

    jdbcClient.sql("""
            INSERT INTO contact (conversation_id, user_id, contact_user_id, status)
            VALUES (:conversationId, :userId, :contactUserId, :status)
            """)
        .param("conversationId", contact.getConversationId())
        .param("userId", contact.getUserId())
        .param("contactUserId", contact.getContactUserId())
        .param("status", contact.getStatus().name())
        .update();
  }

  @Override
  public void updateContact(Contact contact) {

    int rowsUpdated = jdbcClient.sql("""
            UPDATE contact
            SET status = :status
            WHERE user_id = :userId AND contact_user_id = :contactUserId
            """)
        .param("userId", contact.getUserId())
        .param("contactUserId", contact.getContactUserId())
        .param("status", contact.getStatus().name())
        .update();

    if (rowsUpdated != 1) {
      throw new ContactNotFoundException();
    }
  }

  @Override
  public Optional<Contact> findContact(String userId, String contactUserId) {

    return jdbcClient.sql("""
            SELECT conversation_id, status
            FROM contact
            WHERE user_id = :userId AND contact_user_id = :contactUserId
            """)
        .param("userId", userId)
        .param("contactUserId", contactUserId)
        .query((rs, rn) -> Contact.builder()
            .conversationId(UUID.fromString(rs.getString("conversation_id")))
            .userId(userId)
            .contactUserId(contactUserId)
            .status(ContactStatus.valueOf(rs.getString("status")))
            .build())
        .optional();
  }

  @Override
  public List<ContactView> findContacts(String userId, Iterable<ContactStatus> statuses) {

    Set<String> contactStatuses = new HashSet<>();
    statuses.forEach(s -> contactStatuses.add(s.name()));

    return jdbcClient.sql("""
        SELECT c.conversation_id, u.id, u.username, c.status
        FROM contact c
        JOIN users u
        ON c.contact_user_id = u.id
        WHERE c.user_id = :userId AND c.status IN (:statuses)
        """)
        .param("userId", userId)
        .param("statuses", contactStatuses)
        .query((rs, rn) -> new ContactView(
            UUID.fromString(rs.getString("conversation_id")),
            rs.getString("id"),
            rs.getString("username"),
            ContactStatus.valueOf(rs.getString("status"))
        ))
        .list();
  }

}

package com.mikhailkarpov.backend.users.db;

import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserRepository implements UserRepository {

  private final JdbcClient jdbcClient;

  public JdbcUserRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Override
  public boolean existsById(String userId) {
    return jdbcClient.sql("""
        SELECT username FROM users WHERE id = :id
        """)
        .param("id", userId)
        .query(String.class)
        .optional()
        .isPresent();
  }

  @Override
  public Optional<User> findById(String userId) {

    return jdbcClient.sql("""
            SELECT u.id, u.username, u.display_name
            FROM users u
            WHERE u.id = :id
            """)
        .param("id", userId)
        .query(User.class)
        .optional();
  }

  @Override
  public List<User> findByUsername(String query) {

    return jdbcClient.sql("""
            SELECT u.id, u.username, u.display_name
            FROM users u
            WHERE u.username = :query
            ORDER BY u.username
            """)
        .param("query", query)
        .query(User.class)
        .list();
  }

  @Override
  public User save(User user) {
    jdbcClient.sql("""
            INSERT INTO users (id, username, display_name)
            VALUES (:id, :username, :displayName)
            ON CONFLICT (id) DO UPDATE SET
            username = :username,
            display_name = :displayName
            """)
        .param("id", user.id())
        .param("username", user.username())
        .param("displayName", user.displayName())
        .update();
    return user;
  }

}

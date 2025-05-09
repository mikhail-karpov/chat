package com.mikhailkarpov.backend.users.db;

import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserRepository;
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
  public Optional<User> findById(String userId) {

    return jdbcClient.sql("""
            SELECT u.id, u.username
            FROM users u
            WHERE u.id = :id
            """)
        .param("id", userId)
        .query(User.class)
        .optional();
  }

  @Override
  public User save(User user) {
    jdbcClient.sql("""
            INSERT INTO users (id, username)
            VALUES (:id, :username)
            """)
        .param("id", user.id())
        .param("username", user.username()).update();
    return user;
  }

}

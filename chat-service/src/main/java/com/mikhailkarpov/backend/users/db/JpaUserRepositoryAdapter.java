package com.mikhailkarpov.backend.users.db;

import com.mikhailkarpov.backend.users.User;
import com.mikhailkarpov.backend.users.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class JpaUserRepositoryAdapter implements UserRepository {

  private final EntityManager entityManager;

  public JpaUserRepositoryAdapter(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Optional<User> findById(String userId) {
    UserEntity userEntity = entityManager.find(UserEntity.class, userId);
    return Optional.ofNullable(userEntity).map(UserEntity::toDomain);
  }

  @Override
  public User save(User user) {
    entityManager.persist(UserEntity.from(user));
    return user;
  }

}

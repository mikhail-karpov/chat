package com.mikhailkarpov.backend.users.db;

import com.mikhailkarpov.backend.users.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
public class UserEntity {

  @Id
  private String id;

  @Column(unique = true)
  private String username;

  public static UserEntity from(User user) {
    UserEntity userEntity = new UserEntity();
    userEntity.id = user.id();
    userEntity.username = user.username();
    return userEntity;
  }

  public User toDomain() {
    return new User(id, username);
  }

}

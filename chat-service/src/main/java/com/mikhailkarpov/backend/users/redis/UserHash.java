package com.mikhailkarpov.backend.users.redis;

import com.mikhailkarpov.backend.users.User;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("chat:user")
public class UserHash {

  @Id
  private String id;
  private String username;

  @PersistenceCreator
  public UserHash(String id, String username) {
    this.id = id;
    this.username = username;
  }

  public static UserHash fromDomain(User user) {
    return new UserHash(user.id(), user.username());
  }

  public User toDomain() {
    return new User(id, username);
  }

}

package com.mikhailkarpov.backend.messages.db;

import com.mikhailkarpov.backend.messages.Message;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "userId"})
public class MessageEntity {

  @Id
  private String id;

  private String userId;

  private String text;

  private Instant createdAt;

  public static MessageEntity from(Message message) {
    MessageEntity entity = new MessageEntity();
    entity.id = message.id();
    entity.userId = message.userId();
    entity.text = message.text();
    entity.createdAt = message.createdAt();
    return entity;
  }

}

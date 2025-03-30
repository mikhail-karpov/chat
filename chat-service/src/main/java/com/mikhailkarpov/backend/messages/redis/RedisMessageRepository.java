package com.mikhailkarpov.backend.messages.redis;

import com.mikhailkarpov.backend.messages.Message;
import com.mikhailkarpov.backend.messages.MessageRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisMessageRepository implements MessageRepository {

  private final RedisTemplate<String, Message> redisTemplate;
  private final Duration ttl;

  public RedisMessageRepository(RedisTemplate<String, Message> redisTemplate, Duration ttl) {
    this.redisTemplate = redisTemplate;
    this.ttl = ttl;
  }

  @Override
  public void addMessage(Message message) {

    long score = message.createdAt().toEpochMilli();
    redisTemplate.opsForZSet().add("chat:messages", message, score);
    double expirationScore = Instant.now().minus(ttl).toEpochMilli();
    redisTemplate.opsForZSet().removeRangeByScore("chat:messages", 0, expirationScore);
  }

  @Override
  public List<Message> listMessages(int limit) {

    Set<Message> messages = redisTemplate.opsForZSet().reverseRange("chat:messages", 0, limit - 1);
    return messages != null ? new ArrayList<>(messages) : List.of();
  }
}

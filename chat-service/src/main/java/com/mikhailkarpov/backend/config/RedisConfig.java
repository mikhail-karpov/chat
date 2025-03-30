package com.mikhailkarpov.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikhailkarpov.backend.messages.Message;
import com.mikhailkarpov.backend.messages.redis.RedisMessageRepository;
import com.mikhailkarpov.backend.users.User;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Value("${chat.messages.ttl}")
  private Duration messageTtl;

  @Bean
  public RedisTemplate<String, User> userRedisTemplate(
      ObjectMapper objectMapper, RedisConnectionFactory connectionFactory) {

    return redisTemplate(objectMapper, connectionFactory, User.class);
  }

  @Bean
  RedisTemplate<String, Message> messageRedisTemplate(
      ObjectMapper objectMapper, RedisConnectionFactory connectionFactory) {

    return redisTemplate(objectMapper, connectionFactory, Message.class);
  }

  @Bean
  RedisMessageRepository redisMessageRepository(RedisTemplate<String, Message> redisTemplate) {
    return new RedisMessageRepository(redisTemplate, messageTtl);
  }

  private <T> RedisTemplate<String, T> redisTemplate(
      ObjectMapper objectMapper, RedisConnectionFactory connectionFactory, Class<T> clazz) {

    StringRedisSerializer stringSerializer = new StringRedisSerializer();
    Jackson2JsonRedisSerializer<T> jsonSerializer =
        new Jackson2JsonRedisSerializer<>(objectMapper, clazz);

    RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(connectionFactory);
    redisTemplate.setKeySerializer(stringSerializer);
    redisTemplate.setValueSerializer(jsonSerializer);
    redisTemplate.setHashKeySerializer(stringSerializer);
    redisTemplate.setHashValueSerializer(jsonSerializer);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }
}


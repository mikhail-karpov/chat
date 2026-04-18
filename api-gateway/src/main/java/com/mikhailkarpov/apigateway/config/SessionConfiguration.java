package com.mikhailkarpov.apigateway.config;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson.SecurityJacksonModules;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class SessionConfiguration implements BeanClassLoaderAware {

  private ClassLoader loader;

  @Bean
  public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
    return new JacksonJsonRedisSerializer<>(objectMapper(), Object.class);
  }

  @Override
  public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
    this.loader = classLoader;
  }

  private JsonMapper objectMapper() {
    return JsonMapper.builder().addModules(SecurityJacksonModules.getModules(this.loader)).build();
  }

}

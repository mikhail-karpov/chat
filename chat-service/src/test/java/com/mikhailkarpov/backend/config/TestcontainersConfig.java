package com.mikhailkarpov.backend.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfig {

  @Bean
  @ServiceConnection(name = "redis")
  GenericContainer<?> redisContainer() {
    DockerImageName image = DockerImageName.parse("redis:7.4-alpine");
    return new GenericContainer<>(image).withExposedPorts(6379);
  }

  @Bean
  @ServiceConnection
  PostgreSQLContainer<?> postgresContainer() {
    return new PostgreSQLContainer<>(DockerImageName.parse("postgres:17.4-alpine"));
  }

}

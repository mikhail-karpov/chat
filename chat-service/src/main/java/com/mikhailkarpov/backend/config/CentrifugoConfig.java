package com.mikhailkarpov.backend.config;

import com.mikhailkarpov.backend.config.properties.CentrifugoProperties;
import com.mikhailkarpov.backend.centrifugo.client.CentrifugoClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(CentrifugoProperties.class)
public class CentrifugoConfig {

  @Bean
  CentrifugoClient centrifugoClient(RestClient.Builder builder, CentrifugoProperties properties) {

    var requestFactory = ClientHttpRequestFactoryBuilder.detect().build();

    var restClient = builder
        .requestFactory(requestFactory)
        .baseUrl(properties.url())
        .defaultHeader("X-API-Key", properties.apiKey())
        .build();

    return new CentrifugoClient(restClient);
  }
}

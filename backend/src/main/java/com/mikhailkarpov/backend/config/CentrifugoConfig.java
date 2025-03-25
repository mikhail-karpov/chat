package com.mikhailkarpov.backend.config;

import com.mikhailkarpov.backend.config.properties.CentrifugoProperties;
import com.mikhailkarpov.backend.centrifugo.client.CentrifugoClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(CentrifugoProperties.class)
public class CentrifugoConfig {

  @Bean
  CentrifugoClient centrifugoClient(RestClient.Builder builder, CentrifugoProperties properties) {

    HttpComponentsClientHttpRequestFactory requestFactory =
        new HttpComponentsClientHttpRequestFactory();
    requestFactory.setConnectTimeout(properties.connectTimeout());
    requestFactory.setReadTimeout(properties.readTimeout());

    RestClient restClient = builder
        .requestFactory(requestFactory)
        .baseUrl(properties.url())
        .defaultHeader("X-API-Key", properties.apiKey())
        .build();

    return new CentrifugoClient(restClient);
  }
}

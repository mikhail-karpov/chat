package com.mikhailkarpov.backend.centrifugo.client;

import java.util.Optional;
import org.springframework.web.client.RestClient;

public class CentrifugoClient {

  private final RestClient restClient;

  public CentrifugoClient(RestClient restClient) {
    this.restClient = restClient;
  }

  public <T> void publish(CentrifugoPublishRequest<T> request) {

    CentrifugoPublishResponse response = restClient.post()
        .uri("/api/publish")
        .body(request)
        .retrieve()
        .body(CentrifugoPublishResponse.class);

    Optional.ofNullable(response).map(CentrifugoPublishResponse::error).ifPresent(error -> {
      throw new CentrifugoException(error.message(), error.code());
    });
  }

}

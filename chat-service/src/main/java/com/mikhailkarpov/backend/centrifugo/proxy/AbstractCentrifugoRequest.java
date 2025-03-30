package com.mikhailkarpov.backend.centrifugo.proxy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public abstract sealed class AbstractCentrifugoRequest
    permits CentrifugoConnectRequest, CentrifugoRefreshRequest, CentrifugoSubscribeRequest {

  @NotBlank
  String client;

  @NotBlank
  String transport;

  @NotBlank
  String protocol;

  @NotBlank
  String encoding;

  @JsonCreator(mode = Mode.PROPERTIES)
  protected AbstractCentrifugoRequest(
      @JsonProperty("client") String client,
      @JsonProperty("transport") String transport,
      @JsonProperty("protocol") String protocol,
      @JsonProperty("encoding") String encoding) {

    this.client = client;
    this.transport = transport;
    this.protocol = protocol;
    this.encoding = encoding;
  }

}

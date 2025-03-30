package com.mikhailkarpov.backend.centrifugo.proxy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CentrifugoRefreshRequest extends AbstractCentrifugoRequest {

  @NotBlank
  String user;

  @JsonCreator(mode = Mode.PROPERTIES)
  public CentrifugoRefreshRequest(
      @JsonProperty("client") String client,
      @JsonProperty("transport") String transport,
      @JsonProperty("protocol") String protocol,
      @JsonProperty("encoding") String encoding,
      @JsonProperty("user") String user
  ) {
    super(client, transport, protocol, encoding);
    this.user = user;
  }
}

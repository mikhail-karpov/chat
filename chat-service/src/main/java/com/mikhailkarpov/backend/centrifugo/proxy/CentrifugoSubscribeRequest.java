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
public class CentrifugoSubscribeRequest extends AbstractCentrifugoRequest {

  @NotBlank
  String user;

  @NotBlank
  String channel;

  @JsonCreator(mode = Mode.PROPERTIES)
  public CentrifugoSubscribeRequest(
      @JsonProperty("client") String client,
      @JsonProperty("transport") String transport,
      @JsonProperty("protocol") String protocol,
      @JsonProperty("encoding") String encoding,
      @JsonProperty("user") String user,
      @JsonProperty("channel") String channel
  ) {
    super(client, transport, protocol, encoding);
    this.user = user;
    this.channel = channel;
  }
}

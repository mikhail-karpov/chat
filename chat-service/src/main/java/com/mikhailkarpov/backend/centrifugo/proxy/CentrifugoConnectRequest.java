package com.mikhailkarpov.backend.centrifugo.proxy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CentrifugoConnectRequest extends AbstractCentrifugoRequest {

  @JsonCreator(mode = Mode.PROPERTIES)
  public CentrifugoConnectRequest(
      @JsonProperty("client") String client,
      @JsonProperty("transport") String transport,
      @JsonProperty("protocol") String protocol,
      @JsonProperty("encoding") String encoding
  ) {
    super(client, transport, protocol, encoding);
  }
}

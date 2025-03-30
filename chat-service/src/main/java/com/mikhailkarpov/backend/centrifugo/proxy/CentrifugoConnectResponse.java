package com.mikhailkarpov.backend.centrifugo.proxy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mikhailkarpov.backend.centrifugo.proxy.CentrifugoConnectResponse.Result;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class CentrifugoConnectResponse extends AbstractCentrifugoResponse<Result> {

  public CentrifugoConnectResponse(String user, long expireAt) {
    super(new Result(user, expireAt));
  }

  public record Result(String user, @JsonProperty("expire_at") long expireAt) {

  }
}

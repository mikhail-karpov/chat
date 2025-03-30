package com.mikhailkarpov.backend.centrifugo.proxy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mikhailkarpov.backend.centrifugo.proxy.CentrifugoRefreshResponse.Result;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class CentrifugoRefreshResponse extends AbstractCentrifugoResponse<Result> {

  public CentrifugoRefreshResponse(long expireAt) {
    super(new Result(expireAt));
  }

  public record Result(@JsonProperty("expire_at") long expireAt) {

  }
}

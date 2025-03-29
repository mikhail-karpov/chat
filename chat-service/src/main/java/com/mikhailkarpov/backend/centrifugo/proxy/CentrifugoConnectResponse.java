package com.mikhailkarpov.backend.centrifugo.proxy;

import com.mikhailkarpov.backend.centrifugo.proxy.CentrifugoConnectResponse.Result;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class CentrifugoConnectResponse extends AbstractCentrifugoResponse<Result> {

  public CentrifugoConnectResponse(String user) {
    super(new Result(user));
  }

  public record Result(String user) {

  }
}

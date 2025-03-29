package com.mikhailkarpov.backend.centrifugo.proxy;

import lombok.Getter;

@Getter
public abstract sealed class AbstractCentrifugoResponse<T>
    permits CentrifugoConnectResponse, CentrifugoSubscribeResponse {

  private final T result;

  public AbstractCentrifugoResponse(T result) {
    this.result = result;
  }
}

package com.mikhailkarpov.backend.centrifugo.client;

public class CentrifugoException extends RuntimeException {

  private final Integer code;

  public CentrifugoException(String message, Integer code) {
    super(message);
    this.code = code;
  }

  public Integer getCode() {
    return code;
  }

}

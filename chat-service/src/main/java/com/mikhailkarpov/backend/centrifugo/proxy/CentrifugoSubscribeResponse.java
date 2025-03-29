package com.mikhailkarpov.backend.centrifugo.proxy;

import java.util.Collections;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class CentrifugoSubscribeResponse extends AbstractCentrifugoResponse<Map<String, Object>> {

  public CentrifugoSubscribeResponse() {
    super(Collections.emptyMap());
  }

}

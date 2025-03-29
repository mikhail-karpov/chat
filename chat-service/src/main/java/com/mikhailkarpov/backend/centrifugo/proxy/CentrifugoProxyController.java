package com.mikhailkarpov.backend.centrifugo.proxy;

import com.mikhailkarpov.backend.users.User;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/centrifugo/proxy")
public class CentrifugoProxyController {

  private static final Logger log = LoggerFactory.getLogger(CentrifugoProxyController.class);

  @PostMapping("/connect")
  public CentrifugoConnectResponse proxyConnect(
      @AuthenticationPrincipal User user,
      @Valid @RequestBody CentrifugoConnectRequest request) {

    log.debug("User {} connecting: {}", user, request);
    return new CentrifugoConnectResponse(user.id());
  }

  @PostMapping("/subscribe")
  public CentrifugoSubscribeResponse proxySubscribe(
      @AuthenticationPrincipal User user,
      @Valid @RequestBody CentrifugoSubscribeRequest request) {

    log.debug("User {} subscribes: {}", user, request);
    return new CentrifugoSubscribeResponse();
  }

}

package com.mikhailkarpov.backend.centrifugo.proxy;

import com.mikhailkarpov.backend.config.OpenApiSecurityScheme;
import com.mikhailkarpov.backend.users.User;
import jakarta.validation.Valid;
import java.time.Duration;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/centrifugo/proxy")
@OpenApiSecurityScheme
public class CentrifugoProxyController {

  private static final Logger log = LoggerFactory.getLogger(CentrifugoProxyController.class);

  @Value("${centrifugo.refresh-interval}")
  private Duration refreshInterval;

  @PostMapping("/connect")
  public CentrifugoConnectResponse proxyConnect(
      @AuthenticationPrincipal User user,
      @Valid @RequestBody CentrifugoConnectRequest request) {

    log.debug("User {} connecting: {}", user, request);
    return new CentrifugoConnectResponse(user.id(), getConnectionRefreshEpoch());
  }

  @PostMapping("/refresh")
  public CentrifugoRefreshResponse proxyRefresh(
      @AuthenticationPrincipal User user,
      @Valid @RequestBody CentrifugoRefreshRequest request) {

    log.debug("User {} refreshing: {}", user, request);
    return new CentrifugoRefreshResponse(getConnectionRefreshEpoch());
  }

  @PostMapping("/subscribe")
  public CentrifugoSubscribeResponse proxySubscribe(
      @AuthenticationPrincipal User user,
      @Valid @RequestBody CentrifugoSubscribeRequest request) {

    log.debug("User {} subscribes: {}", user, request);
    return new CentrifugoSubscribeResponse();
  }

  private long getConnectionRefreshEpoch() {
    return Instant.now().plus(refreshInterval).getEpochSecond();
  }
}

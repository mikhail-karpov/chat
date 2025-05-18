package com.mikhailkarpov.apigateway.filters;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TracingFilter implements GlobalFilter, Ordered {

  private final Tracer tracer;
  private final String headerName = "Trace-ID";

  public TracingFilter(@Autowired Tracer tracer) {
    this.tracer = tracer;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

    Optional.ofNullable(tracer.currentSpan())
        .map(Span::context)
        .map(TraceContext::traceId)
        .ifPresent(traceId -> exchange.getResponse().getHeaders().add(headerName, traceId));

    return chain.filter(exchange);
  }

  @Override
  public int getOrder() {
    return -1;
  }

}

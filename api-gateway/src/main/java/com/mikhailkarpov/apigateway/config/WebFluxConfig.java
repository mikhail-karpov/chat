package com.mikhailkarpov.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebFluxConfig implements WebFluxConfigurer {

  @Value("${app.frontend-url}")
  private String frontendUrl;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(this.frontendUrl)
        .allowedHeaders("*")
        .allowedMethods("GET", "POST", "OPTIONS")
        .allowCredentials(true);
  }

}

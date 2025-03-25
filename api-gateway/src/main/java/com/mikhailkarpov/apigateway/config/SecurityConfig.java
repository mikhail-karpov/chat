package com.mikhailkarpov.apigateway.config;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

  @Value("${app.frontend-url:}")
  private String frontendUrl;

  @Bean
  SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

    return http
        .authorizeExchange(auth -> auth
            .pathMatchers("/actuator/health/**").permitAll()
            .anyExchange().authenticated())
        .cors(cors -> cors.configurationSource(configurationSource()))
        .csrf(CsrfSpec::disable)
        .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint()))
        .oauth2Client(Customizer.withDefaults())
        .oauth2Login(login ->
            login.loginPage("/oauth2/authorization/auth-server"))
        .build();
  }

  private UrlBasedCorsConfigurationSource configurationSource() {

    CorsConfiguration cors = new CorsConfiguration();
    if (StringUtils.hasText(this.frontendUrl)) {
      cors.addAllowedOrigin(this.frontendUrl);
    } else {
      cors.addAllowedOrigin("*");
      log.warn("Allowed origins set to '*'");
    }
    cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    cors.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", cors);
    return source;
  }

  private HttpStatusServerEntryPoint authenticationEntryPoint() {

    return new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED);
  }

}

package com.mikhailkarpov.backend.config;

import com.mikhailkarpov.backend.auth.CustomOidcUserService;
import com.mikhailkarpov.backend.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${chat.frontend-url:}")
  private String frontendUrl;

  @Autowired
  private UserRepository userRepository;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    return http
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(ex ->
            ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .oauth2Client(Customizer.withDefaults())
        .oauth2Login(login -> login
            .userInfoEndpoint(userInfoEndpoint ->
                userInfoEndpoint.oidcUserService(oidcUserService()))
            .loginPage("/oauth2/authorization/auth-server")
            .successHandler(authenticationSuccessHandler()))
        .build();
  }

  @Bean
  CustomOidcUserService oidcUserService() {

    return new CustomOidcUserService(this.userRepository);
  }

  @Bean
  SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler() {

    String targetUrl = StringUtils.hasText(this.frontendUrl)
        ? this.frontendUrl
        : "/";

    return new SimpleUrlAuthenticationSuccessHandler(targetUrl);
  }

}

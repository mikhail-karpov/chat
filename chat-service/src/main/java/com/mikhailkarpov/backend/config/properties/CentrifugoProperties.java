package com.mikhailkarpov.backend.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "centrifugo")
public record CentrifugoProperties(

    @URL
    String url,

    @NotBlank
    String apiKey,

    @NotNull
    Duration connectTimeout,

    @NotNull
    Duration readTimeout,

    @NotNull
    Duration refreshInterval
) {

}

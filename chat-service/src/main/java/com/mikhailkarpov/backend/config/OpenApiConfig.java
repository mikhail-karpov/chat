package com.mikhailkarpov.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
@OpenAPIDefinition(
    info = @Info(
        title = "Chat Service",
        version = "0.0.1-SNAPSHOT",
        description = "Documentation for Chat Service"
    )
)
@SecuritySchemes({
    @SecurityScheme(
        name = "security-auth",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
            password = @OAuthFlow(
                authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}",
                tokenUrl = "${springdoc.oAuthFlow.tokenUrl}",
                scopes = {
                    @OAuthScope(name = "openid", description = "OpenID"),
                    @OAuthScope(name = "profile", description = "Profile")
                }
            )
        )
    )
})
public class OpenApiConfig {

}

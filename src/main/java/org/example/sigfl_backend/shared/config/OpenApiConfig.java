package org.example.sigfl_backend.shared.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger UI metadata. Declares a global bearer-JWT security scheme so
 * the "Authorize" button in Swagger UI lets you paste a Keycloak access token.
 * Browse the docs at {@code /swagger-ui.html}.
 */
@Configuration
public class OpenApiConfig {

    private static final String BEARER = "bearer-jwt";

    @Bean
    public OpenAPI sigflOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SIGFL Backend API")
                        .description("REST API for the SIGFL backend")
                        .version("v0.0.1")
                        .license(new License().name("Proprietary")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER))
                .components(new Components().addSecuritySchemes(BEARER,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}

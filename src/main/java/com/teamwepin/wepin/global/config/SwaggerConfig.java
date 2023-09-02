package com.teamwepin.wepin.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.teamwepin.wepin.domain.auth.support.AuthConstants.ACCESS_TOKEN_HEADER;
import static com.teamwepin.wepin.domain.auth.support.AuthConstants.REFRESH_TOKEN_HEADER;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String key = "access Token";
        String refreshKey = "refresh Token";

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(key)
                .addList(refreshKey);

        SecurityScheme accessTokenSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name(ACCESS_TOKEN_HEADER);

        SecurityScheme refreshTokenSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name(REFRESH_TOKEN_HEADER);

        Components components = new Components()
                .addSecuritySchemes(key, accessTokenSecurityScheme)
                .addSecuritySchemes(refreshKey, refreshTokenSecurityScheme);

        return new OpenAPI()
                .addSecurityItem(securityRequirement)

                .components(components)
                .externalDocs(externalDocs())
                .info(apiInfo());
    }

    private ExternalDocumentation externalDocs() {
        return new ExternalDocumentation()
                .description("error code 정리")
                .url("https://docs.google.com/spreadsheets/d/1Vij1gv1tvrZ-tZPNJ7uQQeJkwyu5RCbV7ZGCk3JxaZQ/edit#gid=1681696879");
    }

    private Info apiInfo() {
        return new Info()
                .title("wepin API Documentation")
//                .description()
                .version("1.0.0");
    }

}

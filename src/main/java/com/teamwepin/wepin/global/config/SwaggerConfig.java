package com.teamwepin.wepin.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
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

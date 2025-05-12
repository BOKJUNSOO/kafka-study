package com.spring.seoulmoaapi.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

        @Value("${swagger.server-url}")
        private String serverUrl;

        @Bean
        public OpenAPI openAPI() {
                Server server = new Server();
                server.setUrl(serverUrl);

                return new OpenAPI()
                        .addServersItem(server)
                        .info(new Info()
                                .title("서울모아 API")
                                .description("세션 기반 인증이 적용된 문화행사 플랫폼 API")
                                .version("v1.0.0"))
                        .components(new Components()
                                .addSecuritySchemes("JSESSIONID", new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.COOKIE) // 🔑 쿠키 인증
                                        .name("JSESSIONID")))
                        .addSecurityItem(new SecurityRequirement().addList("JSESSIONID")); // 글로벌 적용
        }
}
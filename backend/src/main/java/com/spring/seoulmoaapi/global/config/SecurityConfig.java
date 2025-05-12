package com.spring.seoulmoaapi.global.config;

import com.spring.seoulmoaapi.domain.member.service.MemberDetailsService;
import com.spring.seoulmoaapi.global.common.security.CustomAccessDeniedHandler;
import com.spring.seoulmoaapi.global.common.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final Environment env;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .exceptionHandling(exception -> exception
                    .authenticationEntryPoint(authenticationEntryPoint) // 401
                    .accessDeniedHandler(accessDeniedHandler)           // 403
            )
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/members/register", "/members/login").permitAll()
                    .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                    .requestMatchers("/events/liked").authenticated()
                    .requestMatchers("/events/**","/categories/**","/weather/**").permitAll()
                    .requestMatchers("/getData/**").permitAll()
                    .requestMatchers("/interaction/event/reviews/event").permitAll()
                    .requestMatchers("/interaction/event/most/liked").permitAll()
                    .anyRequest().authenticated()
            );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        if (List.of(env.getActiveProfiles()).contains("prod")) {
            config.setAllowedOrigins(List.of("http://localhost:3000", "https://seoul-moa.murkui.com")); // 프론트 도메인 추후 설정
        } else {
            config.setAllowedOriginPatterns(List.of("*")); // 테스트용 origin 전체 허용
        }

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
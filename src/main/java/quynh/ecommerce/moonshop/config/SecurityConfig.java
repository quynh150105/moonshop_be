package quynh.ecommerce.moonshop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final CustomJwtDecoder customJwtDecoder;
    private final AccessDeniedHandler accessDeniedHandler;

    private final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/register",
            "/api/auth/login",
            "/api/admin/login",
            "/api/auth/refresh-token",
            "/api/products",
            "/api/products/**",
            "/api/categories",
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
    };

    private final String[] ADMIN_ENDPOINTS = {
            "/api/admin/**"
    };

    private final String[] USER_ENDPOINTS = {
            "/api/cart/**",
            "/api/orders/**",
            "/api/chat/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity){
        httpSecurity.authorizeHttpRequests(request -> {
            request.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                    .requestMatchers(ADMIN_ENDPOINTS).hasRole("ADMIN")
                    .requestMatchers(USER_ENDPOINTS).hasRole("USER")
                    .anyRequest().authenticated();
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.oauth2ResourceServer(
                oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())));


        httpSecurity.exceptionHandling(exception -> exception
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );


        httpSecurity.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}

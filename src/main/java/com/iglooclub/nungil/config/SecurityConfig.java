package com.iglooclub.nungil.config;

import com.iglooclub.nungil.config.jwt.JwtAccessDeniedHandler;
import com.iglooclub.nungil.config.jwt.JwtAuthenticationEntryPoint;
import com.iglooclub.nungil.config.jwt.TokenAuthenticationFilter;
import com.iglooclub.nungil.config.jwt.TokenProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    private final SecurityConfigProperties securityConfigProperties;

    /**
     * 스프링 시큐리티 기능 비활성화
     */
    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring()
                // 정적 리소스에 대한 스프링 시큐리티 사용을 비활성화
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * 특정 HTTP 요청에 대한 웹 기반 보안 구성
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                // CORS 설정
                .cors().configurationSource(corsConfigurationSource())
                .and()

                // 토큰 기반 인증을 사용하기 때문에 세션 기능 비활성화
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // 인증, 인가 설정
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/stomp", "/stomp/**").permitAll()
                .anyRequest().authenticated()
                .and()

                // 헤더를 확인할 커스텀 필터 추가
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                // 인증 및 인가 예외 처리
                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler());

        return http.build();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(
                securityConfigProperties.getAllowedOrigins()
        );
        config.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
        );
        config.setAllowedHeaders(
                List.of("*")
        );
        config.setExposedHeaders(
                List.of("Authorization")
        );
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Getter
    @ConfigurationProperties(prefix = "security")
    @ConstructorBinding
    @RequiredArgsConstructor
    private static class SecurityConfigProperties {

        private final List<String> allowedOrigins;
    }
}


package com.example.luto.config;

import com.example.luto.security.TenantContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, TenantContextFilter tenantContextFilter) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/health", "/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/v1/webhooks/**").permitAll()
        .requestMatchers("/v1/**").authenticated()
        .anyRequest().authenticated()
      )
      .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    http.addFilterAfter(tenantContextFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}

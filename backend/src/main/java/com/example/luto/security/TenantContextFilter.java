package com.example.luto.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantContextFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(@jakarta.annotation.Nonnull HttpServletRequest request, @jakarta.annotation.Nonnull HttpServletResponse response, @jakarta.annotation.Nonnull FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String tenantId = request.getHeader("X-Tenant-Id");
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (tenantId == null && auth != null && auth.getPrincipal() instanceof Jwt) {
        Jwt jwt = (Jwt) auth.getPrincipal();
        Object claim = jwt.getClaims().get("tenant_id");
        if (claim != null) tenantId = String.valueOf(claim);
      }
      if (tenantId != null) {
        TenantContext.setTenantId(tenantId);
      }
      filterChain.doFilter(request, response);
    } finally {
      TenantContext.clear();
    }
  }
}

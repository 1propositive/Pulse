package com.example.luto.web;

import com.example.luto.security.TenantContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
public class IdempotencyFilter extends OncePerRequestFilter {

  private final StringRedisTemplate redis;

  public IdempotencyFilter(StringRedisTemplate redis) { this.redis = redis; }

  @Override
  protected boolean shouldNotFilter(@jakarta.annotation.Nonnull HttpServletRequest request) throws ServletException {
    if (!"POST".equalsIgnoreCase(request.getMethod())) return true;
    String path = request.getRequestURI();
    return !(path.endsWith("/v1/signatures") || path.matches(".*/v1/signatures/.*/invite") || path.endsWith("/v1/messaging/send"));
  }

  @Override
  protected void doFilterInternal(@jakarta.annotation.Nonnull HttpServletRequest request, @jakarta.annotation.Nonnull HttpServletResponse response, @jakarta.annotation.Nonnull FilterChain chain)
      throws ServletException, IOException {

    String idem = request.getHeader("Idempotency-Key");
    if (idem == null || idem.isBlank()) {
      response.setStatus(400);
      response.setContentType("application/json");
      response.getWriter().write("{\"code\":\"IDEMPOTENCY_KEY_REQUIRED\",\"message\":\"Header Idempotency-Key é obrigatório\"}");
      return;
    }
    String tenant = TenantContext.getTenantId();
    String body = readBody(request);
    String fingerprint = request.getMethod() + "|" + request.getRequestURI() + "|" + (tenant == null ? "" : tenant) + "|" + DigestUtils.md5DigestAsHex(body.getBytes(StandardCharsets.UTF_8));
    String key = "idem:" + idem + ":" + fingerprint;

    Boolean first = redis.opsForValue().setIfAbsent(key, "reserved", Duration.ofHours(24));
    if (Boolean.FALSE.equals(first)) {
      response.setStatus(409);
      response.setContentType("application/json");
      response.getWriter().write("{\"code\":\"IDEMPOTENT_REPLAY\",\"message\":\"Requisição repetida com a mesma Idempotency-Key\"}");
      return;
    }

    CachedBodyHttpServletResponse wrapped = new CachedBodyHttpServletResponse(response);
    chain.doFilter(new CachedBodyHttpServletRequest(request, body), wrapped);

    byte[] out = wrapped.getBody();
    String storeKey = key + ":resp";
    redis.opsForValue().set(storeKey, new String(out, StandardCharsets.UTF_8), Duration.ofHours(24));
    wrapped.copyBodyToResponse();
  }

  private String readBody(HttpServletRequest request) throws IOException {
    StringBuilder sb = new StringBuilder();
    ServletInputStream is = request.getInputStream();
    byte[] buf = new byte[1024];
    int len;
    while ((len = is.read(buf)) != -1) {
      sb.append(new String(buf, 0, len, StandardCharsets.UTF_8));
    }
    return sb.toString();
  }
}

class CachedBodyHttpServletRequest extends jakarta.servlet.http.HttpServletRequestWrapper {
  private final byte[] cached;
  public CachedBodyHttpServletRequest(HttpServletRequest request, String body) {
    super(request);
    this.cached = body.getBytes(java.nio.charset.StandardCharsets.UTF_8);
  }
  @Override
  public ServletInputStream getInputStream() {
    ByteArrayInputStream bais = new ByteArrayInputStream(cached);
    return new ServletInputStream() {
      public int read() { return bais.read(); }
      public boolean isFinished() { return bais.available() == 0; }
      public boolean isReady() { return true; }
      public void setReadListener(jakarta.servlet.ReadListener readListener) {}
    };
  }
}

class CachedBodyHttpServletResponse extends jakarta.servlet.http.HttpServletResponseWrapper {
  private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
  private final jakarta.servlet.ServletOutputStream outputStream = new jakarta.servlet.ServletOutputStream() {
    @Override public boolean isReady() { return true; }
    @Override public void setWriteListener(jakarta.servlet.WriteListener writeListener) {}
    @Override public void write(int b) throws IOException { bos.write(b); }
  };
  private PrintWriter writer = new PrintWriter(new OutputStreamWriter(bos, java.nio.charset.StandardCharsets.UTF_8), true);

  public CachedBodyHttpServletResponse(HttpServletResponse response) { super(response); }
  @Override public jakarta.servlet.ServletOutputStream getOutputStream() { return outputStream; }
  @Override public PrintWriter getWriter() { return writer; }
  public byte[] getBody() { return bos.toByteArray(); }
  public void copyBodyToResponse() throws IOException {
    byte[] bytes = bos.toByteArray();
    jakarta.servlet.ServletOutputStream os = super.getOutputStream();
    os.write(bytes);
    os.flush();
  }
}

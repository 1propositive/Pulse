package com.example.luto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.interfaces.RSAPublicKey;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {

  @Value("${security.jwt.public-key:}")
  private String publicKeyPem;

  @Bean
  public JwtDecoder jwtDecoder() throws Exception {
    if (publicKeyPem == null || publicKeyPem.isBlank()) {
      throw new IllegalStateException("JWT public key not configured (security.jwt.public-key)");
    }
    String pem = publicKeyPem
      .replace("-----BEGIN PUBLIC KEY-----", "")
      .replace("-----END PUBLIC KEY-----", "")
      .replace("\n", "")
      .replace("\r", "")
      .replaceAll("\\s+", "");
    byte[] keyBytes = Base64.getDecoder().decode(pem);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
    RSAPublicKey rsa = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
    return NimbusJwtDecoder.withPublicKey(rsa).build();
  }
}

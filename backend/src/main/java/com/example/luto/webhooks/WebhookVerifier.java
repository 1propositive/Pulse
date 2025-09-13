package com.example.luto.webhooks;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class WebhookVerifier {

  private final String defaultSecret;

  public WebhookVerifier(@Value("${WEBHOOK_SECRET:}") String defaultSecret) {
    this.defaultSecret = defaultSecret;
  }

  public boolean verify(String payload, String signatureHex, String timestamp) {
    try {
      if (timestamp != null && !timestamp.isBlank()) {
        long ts = Long.parseLong(timestamp);
        long now = System.currentTimeMillis() / 1000L;
        if (Math.abs(now - ts) > 300) return false;
      }
    } catch (Exception ignored) {}

    String secret = (defaultSecret == null || defaultSecret.isBlank()) ? "dev-secret" : defaultSecret;
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
      byte[] h = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
      String expected = Hex.encodeHexString(h);
      return expected.equalsIgnoreCase(signatureHex);
    } catch (Exception e) {
      return false;
    }
  }
}

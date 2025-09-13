package com.example.luto.controller;

import com.example.luto.webhooks.WebhookVerifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class WebhooksController {

  private final WebhookVerifier verifier;

  public WebhooksController(WebhookVerifier verifier) {
    this.verifier = verifier;
  }

  @PostMapping("/webhooks/ingest/whatsapp/zapi")
  public ResponseEntity<Void> zapi(@RequestBody String payload,
      @RequestHeader(value="X-Signature", required=false) String sig,
      @RequestHeader(value="X-Request-Timestamp", required=false) String ts) {
    if (sig != null && !verifier.verify(payload, sig, ts)) return ResponseEntity.status(401).build();
    return ResponseEntity.accepted().build();
  }

  @PostMapping("/webhooks/ingest/whatsapp/wazzup")
  public ResponseEntity<Void> wazzup(@RequestBody String payload,
      @RequestHeader(value="X-Signature", required=false) String sig,
      @RequestHeader(value="X-Request-Timestamp", required=false) String ts) {
    if (sig != null && !verifier.verify(payload, sig, ts)) return ResponseEntity.status(401).build();
    return ResponseEntity.accepted().build();
  }

  @PostMapping("/webhooks/ingest/sms/sonax")
  public ResponseEntity<Void> sonax(@RequestBody String payload,
      @RequestHeader(value="X-Signature", required=false) String sig,
      @RequestHeader(value="X-Request-Timestamp", required=false) String ts) {
    if (sig != null && !verifier.verify(payload, sig, ts)) return ResponseEntity.status(401).build();
    return ResponseEntity.accepted().build();
  }

  @PostMapping("/webhooks/ingest/email/o365")
  public ResponseEntity<Void> email(@RequestBody String payload,
      @RequestHeader(value="X-Signature", required=false) String sig,
      @RequestHeader(value="X-Request-Timestamp", required=false) String ts) {
    if (sig != null && !verifier.verify(payload, sig, ts)) return ResponseEntity.status(401).build();
    return ResponseEntity.accepted().build();
  }

  @PostMapping("/webhooks/ingest/signature/tsa")
  public ResponseEntity<Void> tsa(@RequestBody String payload,
      @RequestHeader(value="X-Signature", required=false) String sig,
      @RequestHeader(value="X-Request-Timestamp", required=false) String ts) {
    if (sig != null && !verifier.verify(payload, sig, ts)) return ResponseEntity.status(401).build();
    return ResponseEntity.accepted().build();
  }
}

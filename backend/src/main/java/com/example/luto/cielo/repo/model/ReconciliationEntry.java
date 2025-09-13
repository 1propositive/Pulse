package com.example.luto.cielo.repo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "reconciliation_entries")
public class ReconciliationEntry {
  @Id
  private String id;

  @Column(name="tenant_id", nullable = false)
  private String tenantId;

  @Column(name="payment_id")
  private String paymentId;

  @Column(name="cielo_tid")
  private String cieloTid;

  @Column(name="nsu")
  private String nsu;

  @Column(name="amount")
  private BigDecimal amount;

  @Column(name="fee")
  private BigDecimal fee;

  @Column(name="status", nullable = false)
  private String status;

  @Column(name="matched_at")
  private Instant matchedAt;

  @Column(name="created_at", nullable = false)
  private Instant createdAt;

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  public String getTenantId() { return tenantId; }
  public void setTenantId(String tenantId) { this.tenantId = tenantId; }
  public String getPaymentId() { return paymentId; }
  public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
  public String getCieloTid() { return cieloTid; }
  public void setCieloTid(String cieloTid) { this.cieloTid = cieloTid; }
  public String getNsu() { return nsu; }
  public void setNsu(String nsu) { this.nsu = nsu; }
  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }
  public BigDecimal getFee() { return fee; }
  public void setFee(BigDecimal fee) { this.fee = fee; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
  public Instant getMatchedAt() { return matchedAt; }
  public void setMatchedAt(Instant matchedAt) { this.matchedAt = matchedAt; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

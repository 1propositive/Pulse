package com.example.luto.cielo.repo.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "cielo_imports")
public class CieloImport {
  @Id
  private String id;
  @Column(name = "tenant_id", nullable = false)
  private String tenantId;
  @Column(name = "file_name", nullable = false)
  private String fileName;
  @Column(name = "imported_at")
  private Instant importedAt;
  @Column(name = "status", nullable = false)
  private String status;
  @Column(name = "records_count", nullable = false)
  private Integer recordsCount;
  @Column(name = "file_hash")
  private String fileHash;

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  public String getTenantId() { return tenantId; }
  public void setTenantId(String tenantId) { this.tenantId = tenantId; }
  public String getFileName() { return fileName; }
  public void setFileName(String fileName) { this.fileName = fileName; }
  public Instant getImportedAt() { return importedAt; }
  public void setImportedAt(Instant importedAt) { this.importedAt = importedAt; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
  public Integer getRecordsCount() { return recordsCount; }
  public void setRecordsCount(Integer recordsCount) { this.recordsCount = recordsCount; }
  public String getFileHash() { return fileHash; }
  public void setFileHash(String fileHash) { this.fileHash = fileHash; }
}

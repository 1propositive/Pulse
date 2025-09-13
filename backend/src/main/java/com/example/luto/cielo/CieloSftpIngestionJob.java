package com.example.luto.cielo;

import com.example.luto.cielo.repo.CieloImportRepository;
import com.example.luto.cielo.repo.ReconciliationEntryRepository;
import com.example.luto.cielo.repo.model.CieloImport;
import com.example.luto.cielo.repo.model.ReconciliationEntry;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteResourceFilter;
import net.schmizz.sshj.sftp.SFTPClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;

@Component
public class CieloSftpIngestionJob {

  private final CieloSftpProperties props;
  private final CieloImportRepository importRepo;
  private final ReconciliationEntryRepository reconRepo;

  public CieloSftpIngestionJob(CieloSftpProperties props, CieloImportRepository importRepo, ReconciliationEntryRepository reconRepo) {
    this.props = props;
    this.importRepo = importRepo;
    this.reconRepo = reconRepo;
  }

  @Scheduled(cron = "0 */15 * * * *")
  public void poll() {
    try (SSHClient ssh = new SSHClient()) {
      ssh.addHostKeyVerifier(new net.schmizz.sshj.transport.verification.PromiscuousVerifier()); // DEV ONLY - accept all host keys
      ssh.connect(props.getHost(), props.getPort());
      ssh.authPassword(props.getUsername(), props.getPassword());
      try (SFTPClient sftp = ssh.newSFTPClient()) {
        List<net.schmizz.sshj.sftp.RemoteResourceInfo> files = sftp.ls(props.getRemoteDir(), (RemoteResourceFilter) f -> f.isRegularFile());
        for (net.schmizz.sshj.sftp.RemoteResourceInfo f : files) {
          String fname = f.getName();
          boolean known = importRepo.existsByTenantIdAndFileName(props.getTenantId(), fname);
          if (known) continue;

          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          try (net.schmizz.sshj.sftp.RemoteFile remoteFile = sftp.open(props.getRemoteDir() + "/" + fname)) {
            try (java.io.InputStream inputStream = remoteFile.new RemoteFileInputStream()) {
              inputStream.transferTo(baos);
            }
          }
          byte[] bytes = baos.toByteArray();
          String sha = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));

          CieloImport imp = new CieloImport();
          imp.setId(java.util.UUID.randomUUID().toString());
          imp.setTenantId(props.getTenantId());
          imp.setFileName(fname);
          imp.setImportedAt(Instant.now());
          imp.setStatus("IMPORTED");
          imp.setRecordsCount(0);
          imp.setFileHash(sha);
          importRepo.save(imp);

          String text = new String(bytes, StandardCharsets.UTF_8);
          for (String line : text.split("\r?\n")) {
            if (line.contains("TID=") && line.contains("VALOR=")) {
              ReconciliationEntry r = new ReconciliationEntry();
              r.setId(java.util.UUID.randomUUID().toString());
              r.setTenantId(props.getTenantId());
              r.setCieloTid(extract(line, "TID"));
              r.setNsu(extract(line, "NSU"));
              try { r.setAmount(new java.math.BigDecimal(extract(line, "VALOR"))); } catch (Exception ignored) {}
              r.setStatus("UNMATCHED");
              r.setCreatedAt(Instant.now());
              reconRepo.save(r);
            }
          }
        }
      }
    } catch (Exception e) {
      System.err.println("[CIELO] Falha na ingestão: " + e.getMessage());
    }
  }

  private String extract(String line, String key) {
    int i = line.indexOf(key + "=");
    if (i < 0) return null;
    int j = line.indexOf(";", i);
    String v = (j > i) ? line.substring(i + key.length() + 1, j) : line.substring(i + key.length() + 1);
    return v.trim();
  }
}

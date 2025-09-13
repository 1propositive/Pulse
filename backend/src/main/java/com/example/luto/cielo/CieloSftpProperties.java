package com.example.luto.cielo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cielo.sftp")
public class CieloSftpProperties {
  private String host = "localhost";
  private int port = 22;
  private String username = "user";
  private String password = "pass";
  private String remoteDir = "/edi";
  private String tenantId = "00000000-0000-0000-0000-000000000001";

  public String getHost() { return host; }
  public void setHost(String host) { this.host = host; }
  public int getPort() { return port; }
  public void setPort(int port) { this.port = port; }
  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }
  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }
  public String getRemoteDir() { return remoteDir; }
  public void setRemoteDir(String remoteDir) { this.remoteDir = remoteDir; }
  public String getTenantId() { return tenantId; }
  public void setTenantId(String tenantId) { this.tenantId = tenantId; }
}

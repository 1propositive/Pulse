package com.example.luto.security;

public class TenantContext {
  private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();
  public static void setTenantId(String tenantId) { CURRENT.set(tenantId); }
  public static String getTenantId() { return CURRENT.get(); }
  public static void clear() { CURRENT.remove(); }
}

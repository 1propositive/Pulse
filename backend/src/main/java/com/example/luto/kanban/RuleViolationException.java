package com.example.luto.kanban;

public class RuleViolationException extends RuntimeException {
  private final String code;
  public RuleViolationException(String code, String message) { super(message); this.code = code; }
  public String getCode() { return code; }
}

package com.example.luto.dto;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record Card(
    UUID id,
    UUID boardId,
    UUID columnId,
    String title,
    Map<String, Object> data,
    Instant createdAt,
    Instant updatedAt,
    String status,
    String assignee
) {}

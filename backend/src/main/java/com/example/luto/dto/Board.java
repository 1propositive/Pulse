package com.example.luto.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Board(
    UUID id,
    String name,
    String description,
    Instant createdAt,
    Instant updatedAt,
    java.util.List<Column> columns
) {}

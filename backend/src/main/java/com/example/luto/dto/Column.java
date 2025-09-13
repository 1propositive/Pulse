package com.example.luto.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Column(
    UUID id,
    UUID boardId,
    String name,
    String category,
    int position,
    Instant createdAt,
    Instant updatedAt,
    java.util.List<Card> cards
) {}

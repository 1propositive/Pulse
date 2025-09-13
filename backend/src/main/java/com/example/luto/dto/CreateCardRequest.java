package com.example.luto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

public record CreateCardRequest(
    @NotNull UUID boardId,
    @NotNull UUID columnId,
    @NotBlank String title,
    Map<String, Object> data,
    String assignee
) {}

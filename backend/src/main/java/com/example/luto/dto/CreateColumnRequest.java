package com.example.luto.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateColumnRequest(
    @NotBlank String name,
    @NotBlank String category,
    int position
) {}

package com.example.luto.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MoveCardRequest(
    @NotNull UUID targetColumnId,
    int position,
    String justification
) {}

package com.oldguard.rickandmortyapi.dtos;

import com.oldguard.rickandmortyapi.annotations.ValidStatus;
import jakarta.validation.constraints.NotEmpty;

public record EditCharacterDTO(
        @NotEmpty(message = "name can't be null/empty")
        String name,
        @NotEmpty(message = "description can't be null or empty")
        String description,
        @ValidStatus
        String status
) {
}

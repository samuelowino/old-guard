package com.oldguard.rickandmortyapi.dtos;

import com.oldguard.rickandmortyapi.annotations.ValidStatus;
import jakarta.validation.constraints.NotEmpty;

public record CreateCharacterDTO(

        @NotEmpty(message = "name must not be empty/null")
        String name,
        @NotEmpty(message = "description must not be null/empty")
        String description,
        @ValidStatus
        String status
) {

}

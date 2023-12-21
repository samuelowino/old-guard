package com.oldguard.rickandmortyapi.dtos;

import com.oldguard.rickandmortyapi.entities.ShowCharacter;

import java.util.List;

public record CharactersDTO(
    Info info,
    List<ShowCharacter> results
) {
}

package com.oldguard.rickandmortyapi.object_creation_tests;

import com.oldguard.rickandmortyapi.entities.ShowCharacter;
import org.junit.jupiter.api.Test;

public class CreateCharacterTest {

    @Test
    public void shouldCreateCharacterTest(){
        var id = 1L;
        var name = "Rick Sanchez";
        var description = "Mad scientist that can travel between dimensions";
        var status = "alive";

        var character = new ShowCharacter(
                id,
                name,
                description,
                status
        );
    }
}

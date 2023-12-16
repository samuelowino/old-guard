package com.oldguard.rickandmortyapi;

import com.oldguard.rickandmortyapi.entities.ShowCharacter;

public class DataDump {

    public static ShowCharacter showCharacter(){
        var id = 1L;
        var name = "Rick Sanchez";
        var description = "Mad scientist that can travel between dimensions";
        var status = "alive";

        return new ShowCharacter(
                id,
                name,
                description,
                status
        );
    }
}

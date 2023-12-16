package com.oldguard.rickandmortyapi.persistence_tests;

import com.oldguard.rickandmortyapi.DataDump;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class PersistShowCharacterTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("This tests that we can actually persist `ShowCharacter` in the db")
    @Test
    public void shouldPersistShowCharacterTest(){
        var showCharacter = DataDump.showCharacter();
        showCharacter.setId(null);
        testEntityManager.persistAndFlush(showCharacter);
    }
}

package com.oldguard.rickandmortyapi.repository_tests;

import com.oldguard.rickandmortyapi.DataDump;
import com.oldguard.rickandmortyapi.entities.ShowCharacter;
import com.oldguard.rickandmortyapi.repository.CharacterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CharacterRepositoryTest {
    //@Inject
    @Autowired
    private CharacterRepository repository;

    @Test
    public void shouldSaveCharacterTest(){
        var character = DataDump.showCharacter();
        repository.save(character);
        var entity = repository.findById(character.getId());

        System.out.println("entity " + entity);
    }

    @Test
    public void shouldFindCharacterByNameTest(){
        var character = DataDump.showCharacter();
        repository.save(character);
        Optional<ShowCharacter> result = repository.findByName("Rick Sanchez");

        assertThat(result).isNotEmpty();

        ShowCharacter entityCharacter = result.get();

        assertThat(entityCharacter).isNotNull();
        assertThat(entityCharacter.getName()).isEqualTo("Rick Sanchez");

    }

}

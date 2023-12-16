package com.oldguard.rickandmortyapi.object_creation_tests;

import com.oldguard.rickandmortyapi.repository.CharacterRepository;
import com.oldguard.rickandmortyapi.service.CharacterManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

public class CharacterManagementServiceTest {

    @MockBean
    private CharacterRepository repository;

    @Test
    public void shouldCreateCharacterManagementServiceTest(){
        var service = new CharacterManagementService();
    }
}

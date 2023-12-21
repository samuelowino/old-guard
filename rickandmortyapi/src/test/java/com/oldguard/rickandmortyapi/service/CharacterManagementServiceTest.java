package com.oldguard.rickandmortyapi.service;

import com.oldguard.rickandmortyapi.DataDump;
import com.oldguard.rickandmortyapi.dtos.CharactersDTO;
import com.oldguard.rickandmortyapi.dtos.CreateCharacterDTO;
import com.oldguard.rickandmortyapi.dtos.EditCharacterDTO;
import com.oldguard.rickandmortyapi.entities.ShowCharacter;
import com.oldguard.rickandmortyapi.repository.CharacterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@SpringBootTest
class CharacterManagementServiceTest {

    private Pageable pageable;
    private PageImpl<ShowCharacter> mockPageCharacters;

    private ShowCharacter character;
     private ShowCharacter characterTwo;
    @Autowired
    private CharacterManagementService characterManagementService;

    @MockBean
    private CharacterRepository characterRepository;

    @BeforeEach
    void setUp(){

        character = DataDump.showCharacter();

        characterTwo = new ShowCharacter();
            characterTwo.setId(2L);
            characterTwo.setName("Squanchy");
            characterTwo.setDescription("A cat-like creature with a unique way of speaking.");
            characterTwo.setStatus("dead");

        pageable = PageRequest.of(0, 20);
        mockPageCharacters = new PageImpl<>(List.of(character, characterTwo));

    }
    @Test
    void shouldCreateCharacterTest() {
        


        //mocking the repository method
        when(characterRepository.save(any(ShowCharacter.class))).thenReturn(character);

        CreateCharacterDTO createCharacterDTO = new CreateCharacterDTO("Rick Sanchez","Mad scientist that can travel between dimensions","alive");

        ShowCharacter characterActual = characterManagementService.saveCharacter(createCharacterDTO);

        assertNotNull(characterActual);
        assertEquals("Rick Sanchez",characterActual.getName());
        assertEquals("alive", characterActual.getStatus());


    }

     @Test
    void shouldEditCharacterTest() {


        EditCharacterDTO editCharacterDTO = new EditCharacterDTO("Rick Smith","A boring version of Rick","dead");
        ShowCharacter editedCharacter = new ShowCharacter(
            1L,editCharacterDTO.name(),editCharacterDTO.description(),editCharacterDTO.status()
        );

        //mocking the repository method
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));
        when(characterRepository.save(editedCharacter)).thenReturn(editedCharacter);

        
        ShowCharacter characterActual = characterManagementService.editCharacter(editCharacterDTO,"1");

        assertEquals("Rick Smith",characterActual.getName());
        assertEquals("dead", characterActual.getStatus());


    }


    @Test
    void shouldFindByNameTest() {
        when(characterRepository.findByName(any(String.class))).thenReturn(Optional.ofNullable(character));

        ShowCharacter characterActual = characterManagementService.findByName("Rick Sanchez");

        assertNotNull(characterActual);
        assertEquals("Rick Sanchez",characterActual.getName());
        assertEquals("alive", characterActual.getStatus());

    }
    @Test
    void shouldThrowEntityNOtFoundExceptionTest(){

        when(characterRepository.findByName(any(String.class))).thenThrow(EntityNotFoundException.class);


        assertThrows(EntityNotFoundException.class, ()-> characterManagementService.findByName("A character's name that doesn't exist"));

    }

    @Test
    void shouldFindByIdTest() {

        when(characterRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(character));

        ShowCharacter characterActual = characterManagementService.findById("1");

        assertNotNull(characterActual);
        assertEquals("Rick Sanchez",characterActual.getName());
        assertEquals("alive", characterActual.getStatus());
        assertEquals(1L, characterActual.getId());

    }

     @Test
    void shouldDeleteByIdTest() {

        

        String mess = characterManagementService.deleteById("1");

        assertEquals("deleted successfully", mess);


    }
     @Test
    void shouldGetAllCharactersTest() {
        //additinal mock data
       

        when(characterRepository.findAll(pageable)).thenReturn(mockPageCharacters);

        CharactersDTO charactersDTO= characterManagementService.getAllCharacters();

        assertNotEquals(0, charactersDTO.results().size());
        assertEquals(2, charactersDTO.results().size());
        verify(characterRepository).findAll(pageable);
    }

    @Test
    void shouldFindCharactersByListOfIds() {
        //additinal mock data
        ShowCharacter characterTwo = new ShowCharacter();
        characterTwo.setId(2L);
        characterTwo.setName("Squanchy");
        characterTwo.setDescription("A cat-like creature with a unique way of speaking.");
        characterTwo.setStatus("dead");

        List<Long> ids = List.of(1L,2L);
        when(characterRepository.findAllById(ids)).thenReturn(List.of(character, characterTwo));

        List<ShowCharacter> characters = characterManagementService.findCharactersByListOfIds("[1,2]");

        assertNotEquals(0, characters.size());
        assertEquals(2, characters.size());
        verify(characterRepository).findAllById(ids);
    }
    //testing the dynamic filter /return all if no filter is provided

    @Test
    void shouldFilterByFieldNameOnlyParamTest() {
      
        PageImpl<ShowCharacter> mockPageCharactersN = new PageImpl<>(List.of(character, characterTwo));

        when(characterRepository.findByNameContainingIgnoreCase("Rick", pageable)).thenReturn(mockPageCharactersN);
              

        CharactersDTO serviceCharacterDTO = characterManagementService.filterByField("0", "Rick", "");

        assertNotNull(serviceCharacterDTO);
        assertEquals(1, serviceCharacterDTO.info().pages());
        assertEquals(2, serviceCharacterDTO.info().count());
        assertEquals(2, serviceCharacterDTO.results().size());
        assertEquals(true, serviceCharacterDTO.results().contains(character));
   

    }

      @Test
    void shouldFilterByFielStatusOnlyParamTest() {
        //additinal mock data
        
        PageImpl<ShowCharacter> mockPageCharactersN = new PageImpl<>(List.of(characterTwo));

        when(characterRepository.findAllByStatus("dead", pageable)).thenReturn(mockPageCharactersN);
              

        CharactersDTO serviceCharacterDTO = characterManagementService.filterByField("0", "", "dead");

        assertNotNull(serviceCharacterDTO);
        assertEquals(1, serviceCharacterDTO.info().pages());
        assertEquals(1, serviceCharacterDTO.info().count());
        assertEquals(1, serviceCharacterDTO.results().size());
        assertEquals(true, serviceCharacterDTO.results().contains(characterTwo));
   

    }

       @Test
    void shouldFilterByFieldByNameAndStatusParamsTest() {
        //additinal mock data
       
        PageImpl<ShowCharacter> mockPageCharactersN = new PageImpl<>(List.of(characterTwo));

        when(characterRepository.findByNameContainingIgnoreCaseAndStatusContainingIgnoreCase("Squanchy","dead", pageable)).thenReturn(mockPageCharactersN);
              

        CharactersDTO serviceCharacterDTO = characterManagementService.filterByField("0", "Squanchy", "dead");

        assertNotNull(serviceCharacterDTO);
        assertEquals(1, serviceCharacterDTO.info().pages());
        assertEquals(1, serviceCharacterDTO.info().count());
        assertEquals(1, serviceCharacterDTO.results().size());
        assertEquals(true, serviceCharacterDTO.results().contains(characterTwo));
   

    }
          @Test
    void shouldFilterByFieldWithZeroParamsTest() {
      

        when(characterRepository.findAll(pageable)).thenReturn(mockPageCharacters);
              

        CharactersDTO serviceCharacterDTO = characterManagementService.filterByField("0","","");

        assertNotNull(serviceCharacterDTO);
        assertEquals(1, serviceCharacterDTO.info().pages());
        assertEquals(2, serviceCharacterDTO.info().count());
        assertEquals(2, serviceCharacterDTO.results().size());
        
   

    }
}
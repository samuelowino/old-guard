package com.oldguard.rickandmortyapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oldguard.rickandmortyapi.DataDump;
import com.oldguard.rickandmortyapi.dtos.CharactersDTO;
import com.oldguard.rickandmortyapi.dtos.CreateCharacterDTO;
import com.oldguard.rickandmortyapi.dtos.EditCharacterDTO;
import com.oldguard.rickandmortyapi.dtos.Info;
import com.oldguard.rickandmortyapi.entities.ShowCharacter;
import com.oldguard.rickandmortyapi.service.CharacterManagementService;
import com.oldguard.rickandmortyapi.utils.Utilities;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShowCharacterController.class)
public class ShowCharacterControllerTest {

    private static final String API_URL = Utilities.API_URI + "character";
    private ShowCharacter showCharacter;
    private CreateCharacterDTO createCharacterDTO;
    private String request;
    ObjectMapper objectMapper;
    @MockBean
    private CharacterManagementService characterManagementService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        showCharacter = DataDump.showCharacter();
        createCharacterDTO = new CreateCharacterDTO(showCharacter.getName(), showCharacter.getDescription(), showCharacter.getStatus());
        objectMapper = new ObjectMapper();
        request = objectMapper.writeValueAsString(createCharacterDTO);

    }

    @Test
    @DisplayName("Test creation of a character with valid fields' values")
    void shouldCreateNewShowCharacterTest() throws Exception {

        when(characterManagementService.saveCharacter(createCharacterDTO)).thenReturn(showCharacter);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(API_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Rick Sanchez"))
                .andDo(print());

    }

    @Test
    @DisplayName("Test editing of character with valid fields' values")
    void shouldEditCharacterTest() throws Exception {
        EditCharacterDTO editCharacterDTO = new EditCharacterDTO("Rick Smith", "A boring version of Rick", "dead");
        ShowCharacter editedCharacter = new ShowCharacter(
                1L, editCharacterDTO.name(), editCharacterDTO.description(), editCharacterDTO.status()
        );

        String editCharacterString = objectMapper.writeValueAsString(editCharacterDTO);
        when(characterManagementService.editCharacter(editCharacterDTO, "1")).thenReturn(editedCharacter);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(API_URL + "/edit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editCharacterString)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Rick Smith"))
                .andDo((print()));
    }

    @Test
    @DisplayName("Test @ExceptionHandler handling EntityNotFoundException thrown when character not found")
    void throwEntityNotFoundExceptionTest() throws Exception {

        when(characterManagementService.findCharactersByIds("1")).thenThrow(new EntityNotFoundException("character  not found"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(API_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo((print()));

    }

    @Test
    @DisplayName("Test @ExceptionHandler handling MethodArgumentsNotValidException thrown when dtos have invalid fields")
    void throwMethodArgumentsNotValidTest() throws Exception {
        CreateCharacterDTO invalidCreateCharacterDTO = new CreateCharacterDTO("", createCharacterDTO.description(), createCharacterDTO.status());
        mockMvc.perform(MockMvcRequestBuilders
                        .post(API_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateCharacterDTO))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andDo((print()));

    }

    @Test
    @DisplayName("Test @ExceptionHandler handling EntityExistsException thrown when trying to save an existing entity")
    void shouldThrowEntityExistsTest() throws Exception {

        when(characterManagementService.saveCharacter(createCharacterDTO)).thenThrow(new EntityExistsException("character already exists"));

        mockMvc.perform(MockMvcRequestBuilders
                        .post(API_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict())
                .andDo((print()));
    }

    @Test
    @DisplayName("Test getting a character when a single id is supplied")
    void testFetchShowCharacterByIdTest() throws Exception {

        when(characterManagementService.findCharactersByIds("1")).thenReturn(showCharacter);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(API_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Rick Sanchez"))
                .andExpect(jsonPath("$.id").value(1L))
                .andDo((print()));
    }

    @Test
    @DisplayName("Test getting all characters matching the ids provided")
    void testFetchShowCharactersByIdsTest() throws Exception {

        ShowCharacter characterTwo = new ShowCharacter();
        characterTwo.setId(2L);
        characterTwo.setName("Squanchy");
        characterTwo.setDescription("A cat-like creature with a unique way of speaking.");
        characterTwo.setStatus("dead");
        when(characterManagementService.findCharactersByIds("1,2")).thenReturn(List.of(showCharacter, characterTwo));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(API_URL + "/1,2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(2)))
                .andExpect(jsonPath("$[1].id").value(characterTwo.getId()))
                .andDo((print()));
    }

    @Test
    @DisplayName("Test getting character by name")
    void testFindShowCharacterByNameTest() throws Exception {

        when(characterManagementService.findByName("Rick Sanchez")).thenReturn(showCharacter);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(API_URL + "/findByName/Rick Sanchez")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Rick Sanchez"))
                .andExpect(jsonPath("$.id").value(1L))
                .andDo((print()));
    }

    @Test
    @DisplayName("Test getting all characters without filters ,just page")
    void getCharactersWithoutFiltersParamsTest() throws Exception {
        ShowCharacter characterTwo = new ShowCharacter();
        characterTwo.setId(2L);
        characterTwo.setName("Squanchy");
        characterTwo.setDescription("A cat-like creature with a unique way of speaking.");
        characterTwo.setStatus("dead");

        Info info = new Info(2, 1, null, null);
        List<ShowCharacter> characters = List.of(characterTwo, showCharacter);

        CharactersDTO charactersDTO = new CharactersDTO(info, characters);

        when(characterManagementService.filterByField("0", "", "")).thenReturn(charactersDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(API_URL + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results").value(hasSize(2)))
                .andExpect(jsonPath("$.results[0].id").value(characterTwo.getId()))
                .andExpect(jsonPath("$.info.count").value(2))
                .andDo((print()));
    }

    @Test
    @DisplayName("Test getting all characters with name filter only")
    void getCharactersByNameFilterTest() throws Exception {


        Info info = new Info(1, 1, null, null);
        List<ShowCharacter> characters = List.of(showCharacter);

        CharactersDTO charactersDTO = new CharactersDTO(info, characters);

        when(characterManagementService.filterByField("0", "Rick", "")).thenReturn(charactersDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(API_URL + "/?name=Rick")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results").value(hasSize(1)))
                .andExpect(jsonPath("$.results[0].id").value(showCharacter.getId()))
                .andExpect(jsonPath("$.info.count").value(1));
    }

    @Test
    @DisplayName("Test getting all the characters")
    void testGetAllShowCharactersTest() throws Exception {

        ShowCharacter characterTwo = new ShowCharacter();
        characterTwo.setId(2L);
        characterTwo.setName("Squanchy");
        characterTwo.setDescription("A cat-like creature with a unique way of speaking.");
        characterTwo.setStatus("dead");

        Info info = new Info(2, 1, null, null);
        List<ShowCharacter> characters = List.of(characterTwo, showCharacter);

        CharactersDTO charactersDTO = new CharactersDTO(info, characters);

        when(characterManagementService.getAllCharacters()).thenReturn(charactersDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results").value(hasSize(2)))
                .andExpect(jsonPath("$.results[0].id").value(characterTwo.getId()))
                .andExpect(jsonPath("$.info.count").value(2))
                .andDo((print()));
    }

    @Test
    @DisplayName("Test deleting of a character by id")
    void testDeleteCharacterByIdTest() throws Exception {

        when(characterManagementService.deleteById("1")).thenReturn("deleted successfully");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(API_URL + "/delete/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo((print()));
    }
}

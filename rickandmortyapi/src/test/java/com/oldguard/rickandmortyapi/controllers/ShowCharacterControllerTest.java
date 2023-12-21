package com.oldguard.rickandmortyapi.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oldguard.rickandmortyapi.DataDump;
import com.oldguard.rickandmortyapi.dtos.CharactersDTO;
import com.oldguard.rickandmortyapi.dtos.CreateCharacterDTO;
import com.oldguard.rickandmortyapi.dtos.EditCharacterDTO;
import com.oldguard.rickandmortyapi.dtos.Info;
import com.oldguard.rickandmortyapi.entities.ShowCharacter;
import com.oldguard.rickandmortyapi.service.CharacterManagementService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import static org.hamcrest.Matchers.hasSize;


import java.util.List;


@WebMvcTest(ShowCharacterController.class)
public class ShowCharacterControllerTest {

    private static final String API_URL = "/api/v1/character";

    private ShowCharacter showCharacter;

    private  CreateCharacterDTO createCharacterDTO;

    private String request;
    ObjectMapper objectMapper ;

    @MockBean
    private CharacterManagementService characterManagementService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws JsonProcessingException{
        showCharacter = DataDump.showCharacter();

        createCharacterDTO = new CreateCharacterDTO(showCharacter.getName(), showCharacter.getDescription(), showCharacter.getStatus());

        objectMapper = new ObjectMapper();
        request = objectMapper.writeValueAsString(createCharacterDTO);

//        mockMvc = MockMvcBuilders.standaloneSetup(ShowCharacterController.class).setControllerAdvice()
    }


    @Test
    void shouldCreateNewShowCharacterTest() throws Exception {
       

        //make the mocked service return charcter when called 
        when(characterManagementService.saveCharacter(createCharacterDTO)).thenReturn(showCharacter);
        
        mockMvc.perform(MockMvcRequestBuilders
                            .post(API_URL+"/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.name").value("Rick Sanchez"));

        
    }

    @Test
    void shouldEditCharacterTest() throws Exception{
        EditCharacterDTO editCharacterDTO = new EditCharacterDTO("Rick Smith","A boring version of Rick","dead");
        ShowCharacter editedCharacter = new ShowCharacter(
            1L,editCharacterDTO.name(),editCharacterDTO.description(),editCharacterDTO.status()
        );

        String editCharacterString = objectMapper.writeValueAsString(editCharacterDTO);
        when(characterManagementService.editCharacter(editCharacterDTO, "1")).thenReturn(editedCharacter);

        mockMvc.perform(MockMvcRequestBuilders
                                .put(API_URL+"/edit/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(editCharacterString)
                                .accept(MediaType.APPLICATION_JSON)
                        )
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.name").value("Rick Smith"));
    }
     @Test
    void shouldThrowEntityNotFoundExceptionTest() throws Exception{

        when(characterManagementService.findById("1")).thenThrow(new EntityNotFoundException("character  not found"));

        mockMvc.perform(MockMvcRequestBuilders
                            .get(API_URL+"/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            )
                .andExpect(status().isNotFound());
               

    }
    @Test
    void shouldThrowEntityAlreadyExistsTest() throws Exception{

        when(characterManagementService.saveCharacter(createCharacterDTO)).thenThrow(new EntityExistsException("character already exists"));

        mockMvc.perform(MockMvcRequestBuilders
                            .post(API_URL+"/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
                                .accept(MediaType.APPLICATION_JSON)
                            )
                .andExpect(status().isConflict());
               

    }

    @Test
    void testFetchShowCharacterByIdTest() throws Exception{

         when(characterManagementService.findById("1")).thenReturn(showCharacter);
        
        mockMvc.perform(MockMvcRequestBuilders
                            .get(API_URL+"/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.name").value("Rick Sanchez"))
                    .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testFindShowCharacterByNameTest() throws Exception {

         when(characterManagementService.findByName("Rick Sanchez")).thenReturn(showCharacter);
        
        mockMvc.perform(MockMvcRequestBuilders
                            .get(API_URL+"/findByName/Rick Sanchez")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.name").value("Rick Sanchez"))
                    .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetAllShowCharactersWithoutFilterParams() throws Exception{
         ShowCharacter characterTwo = new ShowCharacter();
        characterTwo.setId(2L);
        characterTwo.setName("Squanchy");
        characterTwo.setDescription("A cat-like creature with a unique way of speaking.");
        characterTwo.setStatus("dead");

       
        Info info = new Info(2, 1, null,null);
        List<ShowCharacter> characters = List.of(characterTwo, showCharacter);

        CharactersDTO charactersDTO = new CharactersDTO(info, characters);

         when(characterManagementService.filterByField("0","", "")).thenReturn(charactersDTO);
        
        mockMvc.perform(MockMvcRequestBuilders
                            .get(API_URL+"/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.results").isArray())
                    .andExpect(jsonPath("$.results").value(hasSize(2)))
                    .andExpect(jsonPath("$.results[0].id").value(characterTwo.getId()))
                    .andExpect(jsonPath("$.info.count").value(2));
    }

      @Test
    void testGetAllShowCharactersWithNameFilterOnlyParamTest() throws Exception{
        
        
        Info info = new Info(1, 1, null,null);
        List<ShowCharacter> characters = List.of(showCharacter);

        CharactersDTO charactersDTO = new CharactersDTO(info, characters);

         when(characterManagementService.filterByField("0","Rick", "")).thenReturn(charactersDTO);
        
        mockMvc.perform(MockMvcRequestBuilders
                            .get(API_URL+"/?name=Rick")
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
    void testGetAllShowCharactersTest() throws Exception{
         ShowCharacter characterTwo = new ShowCharacter();
        characterTwo.setId(2L);
        characterTwo.setName("Squanchy");
        characterTwo.setDescription("A cat-like creature with a unique way of speaking.");
        characterTwo.setStatus("dead");

       
        Info info = new Info(2, 1, null,null);
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
                    .andExpect(jsonPath("$.info.count").value(2));
    }


     @Test
    void testDeleteCharacterByIdTest() throws Exception{

         when(characterManagementService.deleteById("1")).thenReturn("deleted successfully");
        
        mockMvc.perform(MockMvcRequestBuilders
                            .delete(API_URL+"/delete/1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }
}

package com.oldguard.rickandmortyapi.service;

import com.oldguard.rickandmortyapi.dtos.CharactersDTO;
import com.oldguard.rickandmortyapi.dtos.CreateCharacterDTO;
import com.oldguard.rickandmortyapi.dtos.EditCharacterDTO;
import com.oldguard.rickandmortyapi.dtos.Info;
import com.oldguard.rickandmortyapi.entities.ShowCharacter;
import com.oldguard.rickandmortyapi.repository.CharacterRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterManagementService {

    private final CharacterRepository characterRepository;

    private static final String API_DOMAIN = "http://domain";

    private Pageable getPageable(String page){
        return PageRequest.of(Integer.parseInt(page),20);
    }

    //save a character
    public ShowCharacter saveCharacter(CreateCharacterDTO createCharacterDTO){
        //check if character already exists
        boolean isExistent = characterRepository.existsByName(createCharacterDTO.name());

        if(isExistent) throw new EntityExistsException();
        
        ShowCharacter showCharacter = new ShowCharacter();
        showCharacter.setDescription(createCharacterDTO.description());
        showCharacter.setName(createCharacterDTO.name());
        showCharacter.setStatus(createCharacterDTO.status());

        return characterRepository.save(showCharacter);
    }

    public ShowCharacter editCharacter(EditCharacterDTO editCharacterDTO, String id){
        ShowCharacter character = characterRepository.findById(Long.parseLong(id)).orElseThrow(
            ()-> new EntityNotFoundException("character not found"));

        if(editCharacterDTO.name() != null && editCharacterDTO.name() != character.getName()){
            character.setName(editCharacterDTO.name());
        }
        if(editCharacterDTO.description() != null && editCharacterDTO.description() != character.getDescription()){
            character.setDescription(editCharacterDTO.description());
        }
        if(editCharacterDTO.status() != null && editCharacterDTO.status() != character.getStatus()){
            character.setStatus(editCharacterDTO.status());
        }
        characterRepository.save(character);
        return character;
        
    }
   

    
    //find character by name
    public ShowCharacter findByName(String name){
        return characterRepository.findByName(name).orElseThrow(
            ()-> new EntityNotFoundException("character  not found"));
    }

    public String deleteById(String id){
        try {
            characterRepository.deleteById(Long.parseLong(id));
            return "deleted successfully";
        } catch (Exception e) {
            //should be handled better
            throw new RuntimeException(e.getMessage());
        }
    }

    //find character by id
    public ShowCharacter findById(String id){
        return characterRepository.findById(Long.parseLong(id)).orElseThrow(
            ()-> new EntityNotFoundException("character  not found"));
    }
    //find  characters matching a list of ids
    public List<ShowCharacter> findCharactersByListOfIds(String ids){
        List<String> idsList;
        if(ids.contains("[")){
            idsList = Arrays.asList(
                    ids.replace("[","").replace("]","").split(",")
            );
        }else{
             idsList = Arrays.asList(ids.split(","));
        }
       

        List<Long> longTypeIds = idsList.stream().map(Long::parseLong).toList();
        return characterRepository.findAllById(longTypeIds);

    }
    //filter by fields passed in the queries or return all if no filter is passed
    public CharactersDTO filterByField(String page, String name, String status){
        //pagination, each page to return 20 characters and page sequence adds by 2 instead of 1
        Pageable pageable = getPageable(page);

        String prev;
        String next;

        if(page.equals("0")){
            prev = null;
        }else {
            prev = String.format("%s/?page=%d&name=%s&status=%s",API_DOMAIN,Integer.parseInt(page)-2,name,status);
        }
        


        if(!name.isEmpty() && !status.isEmpty()){
            Page<ShowCharacter> pageContent = characterRepository.findByNameContainingIgnoreCaseAndStatusContainingIgnoreCase(name,status, pageable);
            List<ShowCharacter> characters = pageContent.toList();
            if(pageContent.getTotalElements()<=20){
               next = null;
            }else{
                 next = String.format("%s/?page=%d&name=%s&status=%s",API_DOMAIN,Integer.parseInt(page)+2,name,status);
            }
            
            Info info = new Info((int) pageContent.getTotalElements(), pageContent.getTotalPages(), next, prev );

            return  new CharactersDTO(info, characters);
        }
        else if(name.isEmpty() && !status.isEmpty()){
            Page<ShowCharacter> pageContent= characterRepository.findAllByStatus(status, pageable);
            List<ShowCharacter> characters = pageContent.toList();
             if(pageContent.getTotalElements()<=20){
                 next = null;
            }else{
                 next = String.format("%s/?page=%d&name=%s&status=%s",API_DOMAIN,Integer.parseInt(page)+2,name,status);
            }
            Info info = new Info((int) pageContent.getTotalElements(), pageContent.getTotalPages(), next, prev );

            return  new CharactersDTO(info, characters);
        }
        else if(!name.isEmpty()){
            Page<ShowCharacter> pageContent= characterRepository.findByNameContainingIgnoreCase(name, pageable);
            List<ShowCharacter> characters = pageContent.toList();
             if(pageContent.getTotalElements()<=20){
                 next = null;
            }else{
                 next = String.format("%s/?page=%d&name=%s&status=%s",API_DOMAIN,Integer.parseInt(page)+2,name,status);
            }
            Info info = new Info((int) pageContent.getTotalElements(), pageContent.getTotalPages(), next, prev );

            return  new CharactersDTO(info, characters);
        }else{
            //mo params
            System.out.println("noparams");
         System.out.println(page+"   page");
            Page<ShowCharacter> pageContent= characterRepository.findAll(pageable);
            List<ShowCharacter> characters = pageContent.toList();
             if(pageContent.getTotalElements()<=20){
                 next = null;
            }else{
                 next = String.format("%s/?page=%d",API_DOMAIN,Integer.parseInt(page)+2);
            }
           
            Info info = new Info((int) pageContent.getTotalElements(), pageContent.getTotalPages(), next, prev );

            return  new CharactersDTO(info, characters);
        }        
    }

    public CharactersDTO getAllCharacters(){
            Pageable pageable = getPageable("0");
            Page<ShowCharacter> pageContent= characterRepository.findAll(pageable);
            List<ShowCharacter> characters = pageContent.toList();
            String next;
             if(pageContent.getTotalElements()<=20){
                 next = null;
            }else{
                 next = String.format("%s/?page=%d",API_DOMAIN,2);
            }
           
            Info info = new Info((int) pageContent.getTotalElements(), pageContent.getTotalPages(), next, null);

            return  new CharactersDTO(info, characters);
    }

   




}

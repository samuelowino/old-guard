package com.oldguard.rickandmortyapi.controllers;

import com.oldguard.rickandmortyapi.dtos.CharactersDTO;
import com.oldguard.rickandmortyapi.dtos.CreateCharacterDTO;
import com.oldguard.rickandmortyapi.dtos.EditCharacterDTO;
import com.oldguard.rickandmortyapi.entities.ShowCharacter;
import com.oldguard.rickandmortyapi.service.CharacterManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/character")
@RequiredArgsConstructor
public class ShowCharacterController {
    //TODO add dtos validations

    private final CharacterManagementService characterManagementService;
    @PostMapping("/create")
    public ResponseEntity<ShowCharacter> createShowCharacter(@RequestBody CreateCharacterDTO createCharacterDTO){
        return new ResponseEntity<>(characterManagementService.saveCharacter(createCharacterDTO), HttpStatus.CREATED);
    }

     @PutMapping("/edit/{id}")
    public ResponseEntity<ShowCharacter> editShowCharacter(@RequestBody EditCharacterDTO editCharacterDTO, @PathVariable String id){
        return new ResponseEntity<>(characterManagementService.editCharacter(editCharacterDTO, id), HttpStatus.OK);
    }

    @GetMapping("/findByName/{name}")
    public ResponseEntity<ShowCharacter> findByName(@PathVariable String name){
        return new ResponseEntity<>(characterManagementService.findByName(name), HttpStatus.OK);
    }

    //can return single ShowCharacter or a list
    //mimics ids handler on original api
    @GetMapping("/{ids}")
    public  ResponseEntity<?> fetchShowCharacter(@PathVariable String ids){
        if(ids.contains(",") || ids.contains("[")){
            return  new ResponseEntity<>(characterManagementService.findCharactersByListOfIds(ids), HttpStatus.OK);
        }else{
            return  new ResponseEntity<>(characterManagementService.findById(ids), HttpStatus.OK);
        }

    }
    //mimics the characters original api endpoint use of filter params 
    @GetMapping("/")
    public ResponseEntity<CharactersDTO> getShowCharactersByFilter(
            @RequestParam(name="page", required = false,defaultValue = "0") String page ,
            @RequestParam(name = "name",required = false, defaultValue = "") String name,
            @RequestParam(name = "status", required = false, defaultValue = "") String status){
        return new ResponseEntity<>(characterManagementService.filterByField(page,name,status), HttpStatus.OK);
    }

      @GetMapping
    public ResponseEntity<CharactersDTO> getShowCharacters(){
        return new ResponseEntity<>(characterManagementService.getAllCharacters(), HttpStatus.OK);
    }

     @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCharacterById(@PathVariable String id){
        return new ResponseEntity<>(characterManagementService.deleteById(id), HttpStatus.OK);
    }
   

}

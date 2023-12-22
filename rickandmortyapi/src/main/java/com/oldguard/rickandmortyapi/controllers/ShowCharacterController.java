package com.oldguard.rickandmortyapi.controllers;

import com.oldguard.rickandmortyapi.dtos.CharactersDTO;
import com.oldguard.rickandmortyapi.dtos.CreateCharacterDTO;
import com.oldguard.rickandmortyapi.dtos.EditCharacterDTO;
import com.oldguard.rickandmortyapi.entities.ShowCharacter;
import com.oldguard.rickandmortyapi.service.CharacterManagementService;
import com.oldguard.rickandmortyapi.utils.Utilities;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Utilities.API_URI + "character")
@RequiredArgsConstructor
public class ShowCharacterController {

    private final CharacterManagementService characterManagementService;

    @PostMapping("/create")
    public ResponseEntity<ShowCharacter> createShowCharacter(@Valid @RequestBody CreateCharacterDTO createCharacterDTO) {
        return new ResponseEntity<>(characterManagementService.saveCharacter(createCharacterDTO), HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ShowCharacter> editShowCharacter(@Valid @RequestBody EditCharacterDTO editCharacterDTO, @PathVariable String id) {
        return new ResponseEntity<>(characterManagementService.editCharacter(editCharacterDTO, id), HttpStatus.OK);
    }

    @GetMapping("/findByName/{name}")
    public ResponseEntity<ShowCharacter> findByName(@PathVariable String name) {
        return new ResponseEntity<>(characterManagementService.findByName(name), HttpStatus.OK);
    }

    /**
     * works the same way as the rick and morty character endpoint with path variables
     *
     * @param ids can be a single id, list of ids or just a comma separated string of ids
     * @return either a {@link  ShowCharacter} if it's a single id or a
     * {@link List}<{@link ShowCharacter}> if more than one id is present
     */
    @GetMapping("/{ids}")
    public ResponseEntity<?> fetchShowCharacter(@PathVariable String ids) {

        return new ResponseEntity<>(characterManagementService.findCharactersByIds(ids), HttpStatus.OK);

    }

    /**
     * mimics the original rick and morty character endpoint with params
     *
     * @param page   for pagination
     * @param name   by which to filter characters by
     * @param status by which to filter characters by
     * @return {@link CharactersDTO}
     */
    @GetMapping("/")
    public ResponseEntity<CharactersDTO> getShowCharactersByFilter(
            @RequestParam(name = "page", required = false, defaultValue = "0") String page,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "status", required = false, defaultValue = "") String status) {
        return new ResponseEntity<>(characterManagementService.filterByField(page, name, status), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CharactersDTO> getShowCharacters() {
        return new ResponseEntity<>(characterManagementService.getAllCharacters(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCharacterById(@PathVariable String id) {
        return new ResponseEntity<>(characterManagementService.deleteById(id), HttpStatus.OK);
    }

}

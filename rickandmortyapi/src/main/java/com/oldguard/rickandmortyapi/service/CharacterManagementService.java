package com.oldguard.rickandmortyapi.service;

import com.oldguard.rickandmortyapi.dtos.CharactersDTO;
import com.oldguard.rickandmortyapi.dtos.CreateCharacterDTO;
import com.oldguard.rickandmortyapi.dtos.EditCharacterDTO;
import com.oldguard.rickandmortyapi.dtos.Info;
import com.oldguard.rickandmortyapi.entities.ShowCharacter;
import com.oldguard.rickandmortyapi.repository.CharacterRepository;
import com.oldguard.rickandmortyapi.utils.Utilities;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${api.domain}")
    private String domain;

    /**
     * Just like the original rickandmortyapi result returned should be paginated
     * with 20 characters per page.
     * Should be noted this means our page count will increment by two
     *
     * @param page for the page number queried
     * @return {@link Pageable}
     */
    private Pageable getPageable(String page) {
        return PageRequest.of(Integer.parseInt(page), 20);
    }

    public ShowCharacter saveCharacter(CreateCharacterDTO createCharacterDTO) {
        //check if character already exists
        boolean isExistent = characterRepository.existsByName(createCharacterDTO.name());

        if (isExistent) throw new EntityExistsException();

        ShowCharacter showCharacter = new ShowCharacter();
        showCharacter.setDescription(createCharacterDTO.description());
        showCharacter.setName(createCharacterDTO.name());
        showCharacter.setStatus(createCharacterDTO.status());

        return characterRepository.save(showCharacter);
    }

    public ShowCharacter editCharacter(EditCharacterDTO editCharacterDTO, String id) {
        ShowCharacter character = characterRepository.findById(Long.parseLong(id)).orElseThrow(
                () -> new EntityNotFoundException("character not found"));

        if (editCharacterDTO.name() != null && !editCharacterDTO.name().equals(character.getName())) {
            character.setName(editCharacterDTO.name());
        }
        if (editCharacterDTO.description() != null && !editCharacterDTO.description().equals(character.getDescription())) {
            character.setDescription(editCharacterDTO.description());
        }
        if (!editCharacterDTO.status().equals(character.getStatus())) {
            character.setStatus(editCharacterDTO.status());
        }
        characterRepository.save(character);
        return character;

    }

    public ShowCharacter findByName(String name) {
        return characterRepository.findByName(name).orElseThrow(
                () -> new EntityNotFoundException("character  not found"));
    }

    public String deleteById(String id) {
        try {
            characterRepository.deleteById(Long.parseLong(id));
            return "deleted successfully";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Object findCharactersByIds(String ids) {

        List<String> idsList;
        if (ids.contains("[") || ids.contains("]")) {
            idsList = Arrays.asList(
                    ids.replace("[", "").replace("]", "").split(",")
            );
            List<Long> longTypeIds = idsList.stream().map(Long::parseLong).toList();
            return characterRepository.findAllById(longTypeIds);
        } else if (ids.contains(",")) {
            idsList = Arrays.asList(ids.split(","));
            List<Long> longTypeIds = idsList.stream().map(Long::parseLong).toList();
            return characterRepository.findAllById(longTypeIds);
        } else {
            return characterRepository.findById(Long.parseLong(ids)).orElseThrow(
                    () -> new EntityNotFoundException("character  not found"));
        }

    }

    public CharactersDTO filterByField(String page, String name, String status) {

        Pageable pageable = getPageable(page);

        String prev;
        String next;

        if (page.equals("0")) {
            prev = null;
        } else {
            prev = Utilities.getPrevURL(domain, page, name, status);
        }

        if (!name.isEmpty() && !status.isEmpty()) {
            Page<ShowCharacter> pageContent = characterRepository.findByNameContainingIgnoreCaseAndStatusContainingIgnoreCase(name, status, pageable);
            List<ShowCharacter> characters = pageContent.toList();
            if (pageContent.getTotalElements() <= 20) {
                next = null;
            } else {
                next = Utilities.getNextURL(domain, page, name, status);
            }

            Info info = new Info((int) pageContent.getTotalElements(), pageContent.getTotalPages(), next, prev);

            return new CharactersDTO(info, characters);
        } else if (name.isEmpty() && !status.isEmpty()) {
            Page<ShowCharacter> pageContent = characterRepository.findAllByStatus(status, pageable);
            List<ShowCharacter> characters = pageContent.toList();
            if (pageContent.getTotalElements() <= 20) {
                next = null;
            } else {
                next = Utilities.getNextURL(domain, page, name, status);
            }
            Info info = new Info((int) pageContent.getTotalElements(), pageContent.getTotalPages(), next, prev);

            return new CharactersDTO(info, characters);
        } else if (!name.isEmpty()) {
            Page<ShowCharacter> pageContent = characterRepository.findByNameContainingIgnoreCase(name, pageable);
            List<ShowCharacter> characters = pageContent.toList();
            if (pageContent.getTotalElements() <= 20) {
                next = null;
            } else {
                next = Utilities.getNextURL(domain, page, name, status);
            }
            Info info = new Info((int) pageContent.getTotalElements(), pageContent.getTotalPages(), next, prev);

            return new CharactersDTO(info, characters);
        } else {

            Page<ShowCharacter> pageContent = characterRepository.findAll(pageable);
            List<ShowCharacter> characters = pageContent.toList();
            if (pageContent.getTotalElements() <= 20) {
                next = null;
            } else {
                next = String.format("%s/?page=%d", domain, Integer.parseInt(page) + 2);
            }

            Info info = new Info((int) pageContent.getTotalElements(), pageContent.getTotalPages(), next, prev);

            return new CharactersDTO(info, characters);
        }
    }

    public CharactersDTO getAllCharacters() {
        Pageable pageable = getPageable("0");
        Page<ShowCharacter> pageContent = characterRepository.findAll(pageable);
        List<ShowCharacter> characters = pageContent.toList();
        String next;
        if (pageContent.getTotalElements() <= 20) {
            next = null;
        } else {
            next = String.format("%s/?page=%d", domain, 2);
        }

        Info info = new Info((int) pageContent.getTotalElements(), pageContent.getTotalPages(), next, null);

        return new CharactersDTO(info, characters);
    }

}

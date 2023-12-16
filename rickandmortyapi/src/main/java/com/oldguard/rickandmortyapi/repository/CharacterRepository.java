package com.oldguard.rickandmortyapi.repository;

import com.oldguard.rickandmortyapi.entities.ShowCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<ShowCharacter, Long> {
    Optional<ShowCharacter> findByName(String name);
}

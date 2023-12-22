package com.oldguard.rickandmortyapi.repository;

import com.oldguard.rickandmortyapi.entities.ShowCharacter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<ShowCharacter, Long> {
    Optional<ShowCharacter> findByName(String name);

    Page<ShowCharacter> findAllByStatus(String status, Pageable pageable);

    Page<ShowCharacter> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<ShowCharacter> findByNameContainingIgnoreCaseAndStatusContainingIgnoreCase(String name, String status, Pageable pageable);

    boolean existsByName(String name);


}

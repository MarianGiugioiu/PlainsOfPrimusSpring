package com.awbd.plainsofprimus.repositories;

import com.awbd.plainsofprimus.domain.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    Boolean existsByName(String name);
    Optional<Character> findByName(String name);
    Optional<Character> findByUser_Username(String username);
    boolean existsByUser_Username(String username);
}

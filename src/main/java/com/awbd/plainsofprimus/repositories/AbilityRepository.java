package com.awbd.plainsofprimus.repositories;

import com.awbd.plainsofprimus.domain.Ability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbilityRepository extends JpaRepository<Ability, Long> {
    Optional<Ability> findByName(String name);
    Page<Ability> findAllByNameLikeOrderByNameAsc(String name, Pageable page);
    Page<Ability> findAllByNameLikeOrderByNameDesc(String name, Pageable page);
    Page<Ability> findAllByNameLike(String name, Pageable page);
}

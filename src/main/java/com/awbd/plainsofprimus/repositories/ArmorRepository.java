package com.awbd.plainsofprimus.repositories;

import com.awbd.plainsofprimus.domain.Armor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArmorRepository extends JpaRepository<Armor, Long> {
    Optional<Armor> findByName(String name);
    Page<Armor> findAllByNameLikeOrderByNameAsc(String name, Pageable page);
    Page<Armor> findAllByNameLikeOrderByNameDesc(String name, Pageable page);
    Page<Armor> findAllByNameLike(String name, Pageable page);
}

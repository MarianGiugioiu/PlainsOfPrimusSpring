package com.awbd.plainsofprimus.repositories;

import com.awbd.plainsofprimus.domain.Weapon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeaponRepository extends JpaRepository<Weapon, Long> {
    Optional<Weapon> findByName(String name);
    Page<Weapon> findAllByNameLikeOrderByNameAsc(String name, Pageable page);
    Page<Weapon> findAllByNameLikeOrderByNameDesc(String name, Pageable page);
    Page<Weapon> findAllByNameLike(String name, Pageable page);
}

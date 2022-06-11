package com.awbd.plainsofprimus.repositories;

import com.awbd.plainsofprimus.domain.Achievement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Optional<Achievement> findByName(String name);
    Page<Achievement> findAllByNameLikeOrderByNameAsc(String name, Pageable page);
    Page<Achievement> findAllByNameLikeOrderByNameDesc(String name, Pageable page);
    Page<Achievement> findAllByNameLike(String name, Pageable page);
}

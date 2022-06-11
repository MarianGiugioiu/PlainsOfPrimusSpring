package com.awbd.plainsofprimus.repositories;

import com.awbd.plainsofprimus.domain.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByName(String name);
    Page<Ticket> findAllByNameLikeOrderByNameAsc(String name, Pageable page);
    Page<Ticket> findAllByNameLikeOrderByNameDesc(String name, Pageable page);
    Page<Ticket> findAllByNameLike(String name, Pageable page);
}

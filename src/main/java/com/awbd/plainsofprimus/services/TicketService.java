package com.awbd.plainsofprimus.services;

import com.awbd.plainsofprimus.domain.Character;
import com.awbd.plainsofprimus.domain.Ticket;
import com.awbd.plainsofprimus.exceptions.ResourceNotFoundException;
import com.awbd.plainsofprimus.repositories.CharacterRepository;
import com.awbd.plainsofprimus.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {
    private final TicketRepository ticketRepository;
    private final CharacterRepository characterRepository;

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Ticket not found.");
                    return new ResourceNotFoundException("Ticket not found");
                }
        );
    }

    public Ticket getTicketByName(String name) {
        return ticketRepository.findByName(name).orElseThrow(
                () -> {
                    log.error("Ticket not found.");
                    return new ResourceNotFoundException("Ticket not found");
                }
        );
    }

    public Ticket addTicket(Ticket ticket, String username) {
        Character character = characterRepository.findByUser_Username(username).orElseThrow(
                () -> {
                    log.error("Weapon not found.");
                    return new ResourceNotFoundException("Weapon not found");
                }
        );
        return ticketRepository.save(Ticket.builder()
                .name(ticket.getName())
                .problem(ticket.getProblem())
                .solution(ticket.getSolution())
                .character(character)
                .build());
    }

    public Ticket updateTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTickets(Integer page, Integer itemsPerPage, String simpleName, String order) {
        String name = "%" + simpleName + "%";

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
        if (order.equals("ASC")) {
            return ticketRepository.findAllByNameLikeOrderByNameAsc(name, pageable).getContent();
        }
        if (order.equals("DESC")) {
            return ticketRepository.findAllByNameLikeOrderByNameDesc(name, pageable).getContent();
        }
        return ticketRepository.findAllByNameLike(name, pageable).getContent();
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}

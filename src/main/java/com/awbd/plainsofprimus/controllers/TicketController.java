package com.awbd.plainsofprimus.controllers;

import com.awbd.plainsofprimus.domain.Ticket;
import com.awbd.plainsofprimus.services.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.awbd.plainsofprimus.DTO.Options;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TicketController {
    private final TicketService ticketService;
    private final Integer itemsPerPage = 6;

    @RequestMapping("/tickets/details/{id}")
    public String getTicketDetails(@PathVariable(name = "id") Long id, Model model){
        Ticket ticket = ticketService.getTicketById(id);
        model.addAttribute("ticket", ticket);
        return "ticket/details";
    }

    @RequestMapping("/tickets/edit/{id}")
    public String getTicketEdit(@PathVariable(name = "id") Long id, Model model){
        Ticket ticket = ticketService.getTicketById(id);
        model.addAttribute("ticket", ticket);
        return "ticket/edit";
    }

    @RequestMapping("/tickets/new")
    public String newTicket(Model model){
        model.addAttribute("ticket", new Ticket());
        return "ticket/create";
    }

    @RequestMapping("/tickets")
    public String showTickets(@RequestParam Integer page, Model model){
        List<Ticket> tickets = ticketService.getTickets(page, itemsPerPage, "", "default");
        model.addAttribute("tickets", tickets);
        model.addAttribute("page", page-1);
        model.addAttribute("itemsPerPage", itemsPerPage);
        model.addAttribute("options", new Options());
        return "ticket/list";
    }

    @RequestMapping("/tickets/options")
    public String showTicketsWithOptions(@RequestParam Integer page, @RequestParam String filter, @RequestParam String sort, Model model){
        log.info(filter);
        log.info(sort);
        List<Ticket> tickets = ticketService.getTickets(page, itemsPerPage, filter, sort);
        model.addAttribute("tickets", tickets);
        model.addAttribute("page", page-1);
        model.addAttribute("itemsPerPage", itemsPerPage);
        model.addAttribute("options", new Options(filter, sort));
        return "ticket/list";
    }

    @RequestMapping("/tickets/red/options")
    public String redirectTicketsWithOptions(@RequestParam Integer page, @ModelAttribute Options options){
        String filter = options.getName();
        String sort = options.getSelectedOrder();
        return ("redirect:/tickets/options/?page=" + page + "&filter=" + filter + "&sort=" + sort);
    }

    @PostMapping("/tickets/update")
    public String updateTicket(@Valid @ModelAttribute Ticket ticket,
                               BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
            return "ticket/edit";
        }

        ticketService.updateTicket(ticket);
        return "redirect:/tickets/edit/" + ticket.getId();
    }

    @PostMapping("/tickets/create")
    public String addTicket(@Valid @ModelAttribute Ticket ticket,
                            BindingResult bindingResult){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (bindingResult.hasErrors()){
            return "ticket/create";
        }

        ticketService.addTicket(ticket, currentPrincipalName);
        return "redirect:/tickets/?page=1" ;
    }

    @RequestMapping("/tickets/delete/{id}")
    public String deleteTicket(@PathVariable(name = "id") Long id){
        ticketService.deleteTicket(id);
        return "redirect:/tickets/?page=1" ;
    }

}

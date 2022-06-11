package com.awbd.plainsofprimus.controllers;

import com.awbd.plainsofprimus.domain.Ability;
import com.awbd.plainsofprimus.services.AbilityService;
import com.awbd.plainsofprimus.services.CharacterService;
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
public class AbilityController {
    private final AbilityService abilityService;
    private final CharacterService characterService;
    private final Integer itemsPerPage = 6;

    @RequestMapping("/abilities/details/{id}")
    public String getAbilityDetails(@PathVariable(name = "id") Long id, Model model){
        Ability ability = abilityService.getAbilityById(id);
        model.addAttribute("ability", ability);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (characterService.existsByUserName(currentPrincipalName)) {
            model.addAttribute("useActive", true);
        } else {
            model.addAttribute("useActive", false);
        }
        return "ability/details";
    }

    @RequestMapping("/abilities/edit/{id}")
    public String getAbilityEdit(@PathVariable(name = "id") Long id, Model model){
        Ability ability = abilityService.getAbilityById(id);
        model.addAttribute("ability", ability);
        return "ability/edit";
    }

    @RequestMapping("/abilities/new")
    public String newAbility(Model model){
        model.addAttribute("ability", new Ability());
        return "ability/create";
    }

    @RequestMapping("/abilities")
    public String showAbilities(@RequestParam Integer page, Model model){
        List<Ability> abilities = abilityService.getAbilities(page, itemsPerPage, "", "default");
        model.addAttribute("abilities", abilities);
        model.addAttribute("page", page-1);
        model.addAttribute("itemsPerPage", itemsPerPage);
        model.addAttribute("options", new Options());
        return "ability/list";
    }

    @RequestMapping("/abilities/options")
    public String showAbilitiesWithOptions(@RequestParam Integer page, @RequestParam String filter, @RequestParam String sort, Model model){
        log.info(filter);
        log.info(sort);
        List<Ability> abilities = abilityService.getAbilities(page, itemsPerPage, filter, sort);
        model.addAttribute("abilities", abilities);
        model.addAttribute("page", page-1);
        model.addAttribute("itemsPerPage", itemsPerPage);
        model.addAttribute("options", new Options(filter, sort));
        return "ability/list";
    }

    @RequestMapping("/abilities/red/options")
    public String redirectAbilitiesWithOptions(@RequestParam Integer page, @ModelAttribute Options options){
        String filter = options.getName();
        String sort = options.getSelectedOrder();
        return ("redirect:/abilities/options/?page=" + page + "&filter=" + filter + "&sort=" + sort);
    }

    @PostMapping("/abilities/update")
    public String updateAbility(@Valid @ModelAttribute Ability ability,
                               BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
            return "ability/edit";
        }

        abilityService.updateAbility(ability);
        return "redirect:/abilities/edit/" + ability.getId();
    }

    @PostMapping("/abilities/create")
    public String addAbility(@Valid @ModelAttribute Ability ability,
                            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
            return "ability/create";
        }

        abilityService.addAbility(ability);
        return "redirect:/abilities/?page=1" ;
    }

    @RequestMapping("/abilities/use/{id}")
    public String useAbility(@PathVariable(name = "id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        abilityService.useAbility(id, currentPrincipalName);
        return "redirect:/abilities/details/" + id;
    }

    @RequestMapping("/abilities/delete/{id}")
    public String deleteAbility(@PathVariable(name = "id") Long id){
        abilityService.deleteAbility(id);
        return "redirect:/abilities/?page=1" ;
    }

}

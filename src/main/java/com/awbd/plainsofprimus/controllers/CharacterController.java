package com.awbd.plainsofprimus.controllers;

import com.awbd.plainsofprimus.DTO.CharacterDTO;
import com.awbd.plainsofprimus.domain.Character;
import com.awbd.plainsofprimus.services.CharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CharacterController {
    private final CharacterService characterService;

    @RequestMapping("/character")
    public String characterOptions(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Character character = characterService.findByUsername(currentPrincipalName);
        if (character == null) {
            return "redirect:/character/new/";
        }
        return "redirect:/character/details/" + character.getName();
    }

    @RequestMapping("/character/new")
    public String newCharacter(Model model){
        model.addAttribute("character", new Character());
        return "character/create";
    }

    @GetMapping("/character/details/{name}")
    public String getCharacterByName(@PathVariable("name") String name, Model model){
        CharacterDTO characterDTO =  characterService.getCharacterByName(name);
        model.addAttribute("character", characterDTO);
        return "character/details";
    }

    @PostMapping("/character-create")
    public String addCharacter(@Valid @ModelAttribute Character character,
                            BindingResult bindingResult
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (bindingResult.hasErrors()){
            return "character/create";
        }

        characterService.addCharacter(character, currentPrincipalName);
        return "redirect:/character/details/" + character.getName();
    }


}

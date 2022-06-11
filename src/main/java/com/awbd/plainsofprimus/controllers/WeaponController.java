package com.awbd.plainsofprimus.controllers;

import com.awbd.plainsofprimus.domain.Weapon;
import com.awbd.plainsofprimus.services.CharacterService;
import com.awbd.plainsofprimus.services.WeaponService;
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
public class WeaponController {
    private final WeaponService weaponService;
    private final CharacterService characterService;
    private final Integer itemsPerPage = 6;

    @RequestMapping("/weapons/details/{id}")
    public String getWeaponDetails(@PathVariable(name = "id") Long id, Model model){
        Weapon weapon = weaponService.getWeaponById(id);
        model.addAttribute("weapon", weapon);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (characterService.existsByUserName(currentPrincipalName)) {
            model.addAttribute("useActive", true);
        } else {
            model.addAttribute("useActive", false);
        }
        return "weapon/details";
    }

    @RequestMapping("/weapons/edit/{id}")
    public String getWeaponEdit(@PathVariable(name = "id") Long id, Model model){
        Weapon weapon = weaponService.getWeaponById(id);
        model.addAttribute("weapon", weapon);
        return "weapon/edit";
    }

    @RequestMapping("/weapons/new")
    public String newWeapon(Model model){
        model.addAttribute("weapon", new Weapon());
        return "weapon/create";
    }

    @RequestMapping("/weapons")
    public String showWeapons(@RequestParam Integer page, Model model){
        List<Weapon> weapons = weaponService.getWeapons(page, itemsPerPage, "", "default");
        model.addAttribute("weapons", weapons);
        model.addAttribute("page", page-1);
        model.addAttribute("itemsPerPage", itemsPerPage);
        model.addAttribute("options", new Options());
        return "weapon/list";
    }

    @RequestMapping("/weapons/options")
    public String showWeaponsWithOptions(@RequestParam Integer page, @RequestParam String filter, @RequestParam String sort, Model model){
        log.info(filter);
        log.info(sort);
        List<Weapon> weapons = weaponService.getWeapons(page, itemsPerPage, filter, sort);
        model.addAttribute("weapons", weapons);
        model.addAttribute("page", page-1);
        model.addAttribute("itemsPerPage", itemsPerPage);
        model.addAttribute("options", new Options(filter, sort));
        return "weapon/list";
    }

    @RequestMapping("/weapons/red/options")
    public String redirectWeaponsWithOptions(@RequestParam Integer page, @ModelAttribute Options options){
        String filter = options.getName();
        String sort = options.getSelectedOrder();
        return ("redirect:/weapons/options/?page=" + page + "&filter=" + filter + "&sort=" + sort);
    }

    @PostMapping("/weapons/update")
    public String updateWeapon(@Valid @ModelAttribute Weapon weapon,
                               BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
            return "weapon/edit";
        }

        weaponService.updateWeapon(weapon);
        return "redirect:/weapons/edit/" + weapon.getId();
    }

    @PostMapping("/weapons/create")
    public String addWeapon(@Valid @ModelAttribute Weapon weapon,
                               BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
            return "weapon/create";
        }

        weaponService.addWeapon(weapon);
        return "redirect:/weapons/?page=1" ;
    }

    @RequestMapping("/weapons/use/{id}")
    public String useWeapon(@PathVariable(name = "id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        weaponService.useWeapon(id, currentPrincipalName);
        return "redirect:/weapons/details/" + id;
    }

    @RequestMapping("/weapons/delete/{id}")
    public String deleteWeapon(@PathVariable(name = "id") Long id){
        weaponService.deleteWeapon(id);
        return "redirect:/weapons/?page=1" ;
    }

}

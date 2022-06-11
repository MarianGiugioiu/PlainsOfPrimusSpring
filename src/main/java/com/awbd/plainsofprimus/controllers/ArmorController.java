package com.awbd.plainsofprimus.controllers;

import com.awbd.plainsofprimus.domain.Armor;
import com.awbd.plainsofprimus.services.ArmorService;
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
public class ArmorController {
    private final ArmorService armorService;
    private final CharacterService characterService;
    private final Integer itemsPerPage = 6;

    @RequestMapping("/armors/details/{id}")
    public String getArmorDetails(@PathVariable(name = "id") Long id, Model model){
        Armor armor = armorService.getArmorById(id);
        model.addAttribute("armor", armor);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (characterService.existsByUserName(currentPrincipalName)) {
            model.addAttribute("useActive", true);
        } else {
            model.addAttribute("useActive", false);
        }
        return "armor/details";
    }

    @RequestMapping("/armors/edit/{id}")
    public String getArmorEdit(@PathVariable(name = "id") Long id, Model model){
        Armor armor = armorService.getArmorById(id);
        model.addAttribute("armor", armor);
        return "armor/edit";
    }

    @RequestMapping("/armors/new")
    public String newArmor(Model model){
        model.addAttribute("armor", new Armor());
        return "armor/create";
    }

    @RequestMapping("/armors")
    public String showArmors(@RequestParam Integer page, Model model){
        List<Armor> armors = armorService.getArmors(page, itemsPerPage, "", "default");
        model.addAttribute("armors", armors);
        model.addAttribute("page", page-1);
        model.addAttribute("itemsPerPage", itemsPerPage);
        model.addAttribute("options", new Options());
        return "armor/list";
    }

    @RequestMapping("/armors/options")
    public String showArmorsWithOptions(@RequestParam Integer page, @RequestParam String filter, @RequestParam String sort, Model model){
        log.info(filter);
        log.info(sort);
        List<Armor> armors = armorService.getArmors(page, itemsPerPage, filter, sort);
        model.addAttribute("armors", armors);
        model.addAttribute("page", page-1);
        model.addAttribute("itemsPerPage", itemsPerPage);
        model.addAttribute("options", new Options(filter, sort));
        return "armor/list";
    }

    @RequestMapping("/armors/red/options")
    public String redirectArmorsWithOptions(@RequestParam Integer page, @ModelAttribute Options options){
        String filter = options.getName();
        String sort = options.getSelectedOrder();
        return ("redirect:/armors/options/?page=" + page + "&filter=" + filter + "&sort=" + sort);
    }

    @PostMapping("/armors/update")
    public String updateArmor(@Valid @ModelAttribute Armor armor,
                               BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
            return "armor/edit";
        }

        armorService.updateArmor(armor);
        return "redirect:/armors/edit/" + armor.getId();
    }

    @PostMapping("/armors/create")
    public String addArmor(@Valid @ModelAttribute Armor armor,
                            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
            return "armor/create";
        }

        armorService.addArmor(armor);
        return "redirect:/armors/?page=1" ;
    }

    @RequestMapping("/armors/use/{id}")
    public String useArmor(@PathVariable(name = "id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        armorService.useArmor(id, currentPrincipalName);
        return "redirect:/armors/details/" + id;
    }

    @RequestMapping("/armors/delete/{id}")
    public String deleteArmor(@PathVariable(name = "id") Long id){
        armorService.deleteArmor(id);
        return "redirect:/armors/?page=1" ;
    }

}

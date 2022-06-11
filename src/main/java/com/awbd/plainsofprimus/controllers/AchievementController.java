package com.awbd.plainsofprimus.controllers;

import com.awbd.plainsofprimus.domain.Achievement;
import com.awbd.plainsofprimus.services.AchievementService;
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
public class AchievementController {
    private final AchievementService achievementService;
    private final CharacterService characterService;
    private final Integer itemsPerPage = 6;

    @RequestMapping("/achievements/details/{id}")
    public String getAchievementDetails(@PathVariable(name = "id") Long id, Model model){
        Achievement achievement = achievementService.getAchievementById(id);
        model.addAttribute("achievement", achievement);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (characterService.existsByUserName(currentPrincipalName)) {
            model.addAttribute("useActive", true);
        } else {
            model.addAttribute("useActive", false);
        }
        return "achievement/details";
    }

    @RequestMapping("/achievements/edit/{id}")
    public String getAchievementEdit(@PathVariable(name = "id") Long id, Model model){
        Achievement achievement = achievementService.getAchievementById(id);
        model.addAttribute("achievement", achievement);
        return "achievement/edit";
    }

    @RequestMapping("/achievements/new")
    public String newAchievement(Model model){
        model.addAttribute("achievement", new Achievement());
        return "achievement/create";
    }

    @RequestMapping("/achievements")
    public String showAchievements(@RequestParam Integer page, Model model){
        List<Achievement> achievements = achievementService.getAchievements(page, itemsPerPage, "", "default");
        model.addAttribute("achievements", achievements);
        model.addAttribute("page", page-1);
        model.addAttribute("itemsPerPage", itemsPerPage);
        model.addAttribute("options", new Options());
        return "achievement/list";
    }

    @RequestMapping("/achievements/options")
    public String showAchievementsWithOptions(@RequestParam Integer page, @RequestParam String filter, @RequestParam String sort, Model model){
        log.info(filter);
        log.info(sort);
        List<Achievement> achievements = achievementService.getAchievements(page, itemsPerPage, filter, sort);
        model.addAttribute("achievements", achievements);
        model.addAttribute("page", page-1);
        model.addAttribute("itemsPerPage", itemsPerPage);
        model.addAttribute("options", new Options(filter, sort));
        return "achievement/list";
    }

    @RequestMapping("/achievements/red/options")
    public String redirectAchievementsWithOptions(@RequestParam Integer page, @ModelAttribute Options options){
        String filter = options.getName();
        String sort = options.getSelectedOrder();
        return ("redirect:/achievements/options/?page=" + page + "&filter=" + filter + "&sort=" + sort);
    }

    @PostMapping("/achievements/update")
    public String updateAchievement(@Valid @ModelAttribute Achievement achievement,
                               BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
            return "achievement/edit";
        }

        achievementService.updateAchievement(achievement);
        return "redirect:/achievements/edit/" + achievement.getId();
    }

    @PostMapping("/achievements/create")
    public String addAchievement(@Valid @ModelAttribute Achievement achievement,
                            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
            return "achievement/create";
        }

        achievementService.addAchievement(achievement);
        return "redirect:/achievements/?page=1" ;
    }

    @RequestMapping("/achievements/use/{id}")
    public String useAchievement(@PathVariable(name = "id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        achievementService.useAchievement(id, currentPrincipalName);
        return "redirect:/achievements/details/" + id;
    }

    @RequestMapping("/achievements/delete/{id}")
    public String deleteAchievement(@PathVariable(name = "id") Long id){
        achievementService.deleteAchievement(id);
        return "redirect:/achievements/?page=1" ;
    }

}

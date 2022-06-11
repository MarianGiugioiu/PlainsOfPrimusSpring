package com.awbd.plainsofprimus.services;

import com.awbd.plainsofprimus.domain.Character;
import com.awbd.plainsofprimus.domain.Achievement;
import com.awbd.plainsofprimus.exceptions.ResourceNotFoundException;
import com.awbd.plainsofprimus.repositories.CharacterRepository;
import com.awbd.plainsofprimus.repositories.AchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final CharacterRepository characterRepository;

    public Achievement getAchievementById(Long id) {
        return achievementRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Achievement not found.");
                    return new ResourceNotFoundException("Achievement not found");
                }
        );
    }

    public Achievement getAchievementByName(String name) {
        return achievementRepository.findByName(name).orElseThrow(
                () -> {
                    log.error("Achievement not found.");
                    return new ResourceNotFoundException("Achievement not found");
                }
        );
    }

    public Achievement addAchievement(Achievement achievement) {
        return achievementRepository.save(Achievement.builder()
                .name(achievement.getName())
                .requirements(achievement.getRequirements())
                .points(achievement.getPoints())
                .build());
    }

    public Achievement updateAchievement(Achievement achievement) {
        return achievementRepository.save(achievement);
    }

    public List<Achievement> getAchievements(Integer page, Integer itemsPerPage, String simpleName, String order) {
        String name = "%" + simpleName + "%";

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
        if (order.equals("ASC")) {
            return achievementRepository.findAllByNameLikeOrderByNameAsc(name, pageable).getContent();
        }
        if (order.equals("DESC")) {
            return achievementRepository.findAllByNameLikeOrderByNameDesc(name, pageable).getContent();
        }
        return achievementRepository.findAllByNameLike(name, pageable).getContent();
    }

    public void useAchievement(Long id, String username) {
        Achievement achievement = achievementRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Achievement not found.");
                    return new ResourceNotFoundException("Achievement not found");
                }
        );
        Character character = characterRepository.findByUser_Username(username).orElseThrow(
                () -> {
                    log.error("Achievement not found.");
                    return new ResourceNotFoundException("Achievement not found");
                }
        );

        List<Achievement> achievements = character.getAchievements();
        if (!achievements.contains(achievement)) {
            achievements.add(achievement);
            character.setAchievements(achievements);
        }
        characterRepository.save(character);
    }

    public void deleteAchievement(Long id) {
        achievementRepository.deleteById(id);
    }
}

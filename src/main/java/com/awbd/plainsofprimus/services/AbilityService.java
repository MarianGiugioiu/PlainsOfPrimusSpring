package com.awbd.plainsofprimus.services;

import com.awbd.plainsofprimus.domain.Character;
import com.awbd.plainsofprimus.domain.Ability;
import com.awbd.plainsofprimus.exceptions.ResourceNotFoundException;
import com.awbd.plainsofprimus.repositories.CharacterRepository;
import com.awbd.plainsofprimus.repositories.AbilityRepository;
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
public class AbilityService {
    private final AbilityRepository abilityRepository;
    private final CharacterRepository characterRepository;

    public Ability getAbilityById(Long id) {
        return abilityRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Ability not found.");
                    return new ResourceNotFoundException("Ability not found");
                }
        );
    }

    public Ability getAbilityByName(String name) {
        return abilityRepository.findByName(name).orElseThrow(
                () -> {
                    log.error("Ability not found.");
                    return new ResourceNotFoundException("Ability not found");
                }
        );
    }

    public Ability addAbility(Ability ability) {
        return abilityRepository.save(Ability.builder()
                .name(ability.getName())
                .scalesWith(ability.getScalesWith())
                .effect(ability.getEffect())
                .build());
    }

    public Ability updateAbility(Ability ability) {
        return abilityRepository.save(ability);
    }

    public List<Ability> getAbilities(Integer page, Integer itemsPerPage, String simpleName, String order) {
        String name = "%" + simpleName + "%";

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
        if (order.equals("ASC")) {
            return abilityRepository.findAllByNameLikeOrderByNameAsc(name, pageable).getContent();
        }
        if (order.equals("DESC")) {
            return abilityRepository.findAllByNameLikeOrderByNameDesc(name, pageable).getContent();
        }
        return abilityRepository.findAllByNameLike(name, pageable).getContent();
    }

    public void useAbility(Long id, String username) {
        Ability ability = abilityRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Ability not found.");
                    return new ResourceNotFoundException("Ability not found");
                }
        );
        Character character = characterRepository.findByUser_Username(username).orElseThrow(
                () -> {
                    log.error("Ability not found.");
                    return new ResourceNotFoundException("Ability not found");
                }
        );

        List<Ability> abilities = character.getAbilities();
        if (!abilities.contains(ability)) {
            abilities.add(ability);
            character.setAbilities(abilities);
        }
        characterRepository.save(character);
    }

    public void deleteAbility(Long id) {
        abilityRepository.deleteById(id);
    }
}

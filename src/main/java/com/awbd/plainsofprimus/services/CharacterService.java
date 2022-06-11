package com.awbd.plainsofprimus.services;

import com.awbd.plainsofprimus.DTO.CharacterDTO;
import com.awbd.plainsofprimus.domain.Ability;
import com.awbd.plainsofprimus.domain.Achievement;
import com.awbd.plainsofprimus.domain.Armor;
import com.awbd.plainsofprimus.domain.Character;
import com.awbd.plainsofprimus.domain.security.User;
import com.awbd.plainsofprimus.exceptions.ResourceNotFoundException;
import com.awbd.plainsofprimus.repositories.CharacterRepository;
import com.awbd.plainsofprimus.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;

    public CharacterDTO getCharacterByName(String name) {
        Character character = characterRepository.findByName(name).orElseThrow(
                () -> {
                    log.error("Weapon not found.");
                    return new ResourceNotFoundException("Character not found");
                }
        );

        List<Armor> armors = new ArrayList<>();
        if(character.getHelmet() != null) {
            armors.add(character.getHelmet());
        }
        if(character.getChestplate() != null) {
            armors.add(character.getChestplate());
        }
        if(character.getLeggings() != null) {
            armors.add(character.getLeggings());
        }
        if(character.getBoots() != null) {
            armors.add(character.getBoots());
        }

        Integer armorValue = 0;
        Integer health = 0;
        Integer strength = 0;
        Integer intellect = 0;
        Integer agility = 0;

        for (Armor armor : armors) {
            armorValue += armor.getArmor();
            health += armor.getHealth();
            strength += armor.getStrength();
            intellect += armor.getIntellect();
            agility += armor.getAgility();
        }

        String abilities = character.getAbilities().stream().map(Ability::getName).collect(Collectors.joining("\n"));
        String achievements = character.getAchievements().stream().map(Achievement::getName).collect(Collectors.joining("\n"));

        log.info(abilities);

        return CharacterDTO.builder()
                .name(character.getName())
                .level(character.getLevel())
                .weapon(character.getWeapon() != null ? character.getWeapon().getName(): "")
                .helmet(character.getHelmet() != null ? character.getHelmet().getName(): "")
                .chestplate(character.getChestplate() != null ? character.getChestplate().getName(): "")
                .leggings(character.getLeggings() != null ? character.getLeggings().getName(): "")
                .boots(character.getBoots() != null ? character.getBoots().getName(): "")
                .armorValue(armorValue)
                .health(health)
                .strength(strength)
                .intellect(intellect)
                .agility(agility)
                .attackDamage(character.getWeapon() != null ? character.getWeapon().getAttackDamage(): 0)
                .abilities(abilities)
                .achievements(achievements)
                .build();
    }

    public boolean existsByUserName(String username) {
        return characterRepository.existsByUser_Username(username);
    }

    public Character findByUsername(String username) {
        return characterRepository.findByUser_Username(username).orElse(null);
    }

    public Character addCharacter(Character character, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("User not found.");
            return new ResourceNotFoundException("User not found");
        });
        return characterRepository.save(Character.builder()
                .name(character.getName())
                .level(character.getLevel())
                .abilities(new ArrayList<>())
                .achievements(new ArrayList<>())
                .tickets(new ArrayList<>())
                .user(user)
                .build());
    }
}

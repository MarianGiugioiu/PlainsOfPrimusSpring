package com.awbd.plainsofprimus.services;

import com.awbd.plainsofprimus.domain.Character;
import com.awbd.plainsofprimus.domain.Armor;
import com.awbd.plainsofprimus.exceptions.ResourceNotFoundException;
import com.awbd.plainsofprimus.repositories.CharacterRepository;
import com.awbd.plainsofprimus.repositories.ArmorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArmorService {
    private final ArmorRepository armorRepository;
    private final CharacterRepository characterRepository;

    public Armor getArmorById(Long id) {
        return armorRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Armor not found.");
                    return new ResourceNotFoundException("Armor not found");
                }
        );
    }

    public Armor getArmorByName(String name) {
        return armorRepository.findByName(name).orElseThrow(
                () -> {
                    log.error("Armor not found.");
                    return new ResourceNotFoundException("Armor not found");
                }
        );
    }

    public Armor addArmor(Armor armor) {
        return armorRepository.save(Armor.builder()
                .name(armor.getName())
                .image(armor.getImage())
                .type(armor.getType())
                .armor(armor.getArmor())
                .health(armor.getHealth())
                .strength(armor.getStrength())
                .intellect(armor.getIntellect())
                .agility(armor.getAgility())
                .build());
    }

    public Armor updateArmor(Armor armor) {
        return armorRepository.save(armor);
    }

    public List<Armor> getArmors(Integer page, Integer itemsPerPage, String simpleName, String order) {
        String name = "%" + simpleName + "%";

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
        if (order.equals("ASC")) {
            return armorRepository.findAllByNameLikeOrderByNameAsc(name, pageable).getContent();
        }
        if (order.equals("DESC")) {
            return armorRepository.findAllByNameLikeOrderByNameDesc(name, pageable).getContent();
        }
        return armorRepository.findAllByNameLike(name, pageable).getContent();
    }

    public void useArmor(Long id, String username) {
        Armor armor = armorRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Armor not found.");
                    return new ResourceNotFoundException("Armor not found");
                }
        );
        Character character = characterRepository.findByUser_Username(username).orElseThrow(
                () -> {
                    log.error("Armor not found.");
                    return new ResourceNotFoundException("Armor not found");
                }
        );

        switch (armor.getType()) {
            case "helmet":
                character.setHelmet(armor);
                break;
            case "chestplate":
                character.setChestplate(armor);
                break;
            case "leggings":
                character.setLeggings(armor);
                break;
            case "boots":
                character.setBoots(armor);
                break;
        }
        characterRepository.save(character);
    }

    public void deleteArmor(Long id) {
        armorRepository.deleteById(id);
    }
}

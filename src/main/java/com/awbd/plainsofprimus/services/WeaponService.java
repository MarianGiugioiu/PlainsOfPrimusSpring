package com.awbd.plainsofprimus.services;

import com.awbd.plainsofprimus.domain.Character;
import com.awbd.plainsofprimus.domain.Weapon;
import com.awbd.plainsofprimus.exceptions.ResourceNotFoundException;
import com.awbd.plainsofprimus.repositories.CharacterRepository;
import com.awbd.plainsofprimus.repositories.WeaponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeaponService {
    private final WeaponRepository weaponRepository;
    private final CharacterRepository characterRepository;

    public Weapon getWeaponById(Long id) {
        return weaponRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Weapon not found.");
                    return new ResourceNotFoundException("Weapon not found");
                }
        );
    }

    public Weapon getWeaponByName(String name) {
        return weaponRepository.findByName(name).orElseThrow(
                () -> {
                    log.error("Weapon not found.");
                    return new ResourceNotFoundException("Weapon not found");
                }
        );
    }

    public Weapon addWeapon(Weapon weapon) {
        return weaponRepository.save(Weapon.builder()
                .name(weapon.getName())
                .image(weapon.getImage())
                .attackDamage(weapon.getAttackDamage())
                .specialBonus(weapon.getSpecialBonus())
                .build());
    }

    public Weapon updateWeapon(Weapon weapon) {
        return weaponRepository.save(weapon);
    }

    public List<Weapon> getWeapons(Integer page, Integer itemsPerPage, String simpleName, String order) {
        String name = "%" + simpleName + "%";

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
        if (order.equals("ASC")) {
            return weaponRepository.findAllByNameLikeOrderByNameAsc(name, pageable).getContent();
        }
        if (order.equals("DESC")) {
            return weaponRepository.findAllByNameLikeOrderByNameDesc(name, pageable).getContent();
        }
        return weaponRepository.findAllByNameLike(name, pageable).getContent();
    }

    public void useWeapon(Long id, String username) {
        Weapon weapon = weaponRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Weapon not found.");
                    return new ResourceNotFoundException("Weapon not found");
                }
        );
        Character character = characterRepository.findByUser_Username(username).orElseThrow(
                () -> {
                    log.error("Weapon not found.");
                    return new ResourceNotFoundException("Weapon not found");
                }
        );

        character.setWeapon(weapon);
        characterRepository.save(character);
    }

    public void deleteWeapon(Long id) {
        weaponRepository.deleteById(id);
    }
}

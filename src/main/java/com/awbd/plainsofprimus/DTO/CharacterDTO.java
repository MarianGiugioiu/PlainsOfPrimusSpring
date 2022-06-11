package com.awbd.plainsofprimus.DTO;

import com.awbd.plainsofprimus.domain.Ability;
import com.awbd.plainsofprimus.domain.Armor;
import com.awbd.plainsofprimus.domain.Ticket;
import com.awbd.plainsofprimus.domain.Weapon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterDTO {
    private String name;

    private Integer level;

    private String weapon;

    private String helmet;

    private String chestplate;

    private String leggings;

    private String boots;

    private String abilities;

    private String achievements;

    private Integer armorValue;

    private Integer health;

    private Integer strength;

    private Integer intellect;

    private Integer agility;

    private Integer attackDamage;
}

package com.awbd.plainsofprimus.domain;

import com.awbd.plainsofprimus.validation.ValidateString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name="abilities")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 30, message = "Name must have between 3 and 30 characters")
    private String name;

    @Column(name = "scales_with")
    @NotBlank(message = "Scale field is mandatory.")
    @ValidateString(acceptedValues={"strength", "intellect", "agility"}, message="Scale field should be strength, intellect, agility.")
    private String scalesWith;

    @Size(max = 255, message = "Effect must have between 0 and 255 characters")
    private String effect;
}

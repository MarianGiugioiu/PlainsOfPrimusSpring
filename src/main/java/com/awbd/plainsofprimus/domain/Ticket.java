package com.awbd.plainsofprimus.domain;

import com.awbd.plainsofprimus.serializers.CharacterSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name="tickets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 30, message = "Name must have between 3 and 30 characters")
    private String name;

    @Size(max = 1000, message = "Problem must have between 0 and 1000 characters")
    private String problem;

    @Size(max = 1000, message = "Solution must have between 0 and 1000 characters")
    private String solution;

    @ManyToOne
    @JsonSerialize(using = CharacterSerializer.class)
    private Character character;
}

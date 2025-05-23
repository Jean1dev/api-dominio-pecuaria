package com.binno.dominio.module.veterinaria.model;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "veterinario")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Veterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @Email
    private String email;
}

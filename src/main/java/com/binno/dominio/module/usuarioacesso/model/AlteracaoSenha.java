package com.binno.dominio.module.usuarioacesso.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "alteracao_senha")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlteracaoSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String chave;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String login;

    @NotNull
    private LocalDateTime dataHoraCriacao;

    //TODO:: Possivel problema de tipos entre diferentes databases aqui
    @NotNull
    @Column//(columnDefinition = "interval")
    private Duration timeExpiracao;
}

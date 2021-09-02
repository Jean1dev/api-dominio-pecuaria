package com.binno.dominio.module.usuarioacesso.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlterarUsuarioDto {

    private Integer id;

    private String nome;

    private String email;

    private String login;

    private String password;

    private String imagemPerfilUrl;

    private String sobrenome;

    private String numero;
}

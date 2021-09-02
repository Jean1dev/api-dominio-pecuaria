package com.binno.dominio.module.usuarioacesso.api.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AlterarUsuarioDto {

    Integer id;

    String nome;

    String email;

    String login;

    String password;

    String imagemPerfilUrl;

    String sobrenome;

    String numero;
}

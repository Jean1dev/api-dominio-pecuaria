package com.binno.dominio.module.usuarioacesso.api.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioAcessoDto {

    @NotNull(message = "login obrigatorio")
    private String login;

    private Integer tenant;

    @NotNull(message = "senha obrigatorio")
    private String password;

    private String nome;

    private String email;
}

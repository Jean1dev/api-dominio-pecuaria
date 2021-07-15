package com.binno.dominio.module.usuarioacesso.api.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioAcessoDto {

    private String login;

    private Integer tenant;

    private String password;

    private Integer pessoa;

    private String nome;

    private String email;
}

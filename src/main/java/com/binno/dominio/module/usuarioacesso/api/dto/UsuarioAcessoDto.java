package com.binno.dominio.module.usuarioacesso.api.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioAcessoDto {

    @NotNull(message = "Login obrigatório")
    @Length(min = 4, max = 200, message = "o comprimento do login deve ser entre 5 e 200")
    private String login;

    private Integer tenant;

    @NotNull(message = "Senha obrigatório")
    @Length(min = 4, max = 200, message = "o comprimento da senha deve ser entre 5 e 200")
    private String password;

    private String nome;

    @NotNull(message = "E-mail obrigatório")
    @Length(min = 5, max = 200, message = "o comprimento do e-mail deve ser entre 5 e 200")
    private String email;
}

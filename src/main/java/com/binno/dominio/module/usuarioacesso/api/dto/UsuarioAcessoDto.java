package com.binno.dominio.module.usuarioacesso.api.dto;

import com.binno.dominio.module.usuarioacesso.model.UsuarioAcesso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@Builder
public final class UsuarioAcessoDto {

    @NotNull(message = "Login obrigatório")
    @Length(min = 4, max = 200, message = "o comprimento do login deve ser entre 5 e 200")
    private final String login;

    private final Integer tenant;

    @NotNull(message = "Senha obrigatório")
    @Length(min = 4, max = 200, message = "o comprimento da senha deve ser entre 5 e 200")
    private final String password;

    private final String nome;

    private final String sobrenome;

    private final String fone;

    @NotNull(message = "E-mail obrigatório")
    @Length(min = 5, max = 200, message = "o comprimento do e-mail deve ser entre 5 e 200")
    private final String email;

    private final String photoURL;

    public static UsuarioAcessoDto toDto(UsuarioAcesso usuarioAcesso) {
        return UsuarioAcessoDto.builder()
                .nome(usuarioAcesso.getNome())
                .photoURL(usuarioAcesso.getImagemPerfilUrl())
                .login(usuarioAcesso.getLogin())
                .email(usuarioAcesso.getEmail())
                .sobrenome(usuarioAcesso.getSobrenome())
                .fone(usuarioAcesso.getNumero())
                .build();
    }
}

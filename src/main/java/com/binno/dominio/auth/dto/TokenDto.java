package com.binno.dominio.auth.dto;

import com.binno.dominio.auth.UsuarioAutenticado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TokenDto {

    private String token;
    private String tipo;
    private UsuarioAutenticado user;
}

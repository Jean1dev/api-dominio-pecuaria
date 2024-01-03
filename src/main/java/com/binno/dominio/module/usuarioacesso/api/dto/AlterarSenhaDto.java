package com.binno.dominio.module.usuarioacesso.api.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public final class AlterarSenhaDto {

    @NotNull
    private String novaSenha;
    @NotNull
    private String chave;
}

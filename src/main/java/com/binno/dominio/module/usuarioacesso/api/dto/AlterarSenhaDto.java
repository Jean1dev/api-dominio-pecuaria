package com.binno.dominio.module.usuarioacesso.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AlterarSenhaDto {

    @NotNull
    private String novaSenha;
    @NotNull
    private String chave;
}

package com.binno.dominio.module.usuarioacesso.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class CriarPedidoAmizadeDto {

    private String mensagem;
    @NotNull(message = "usuario solicitado obrigatório")
    private Integer idUsuarioSolicitante;
    @NotNull(message = "usuario requisitado obrigatório")
    private Integer idUsuarioRequistado;
}

package com.binno.dominio.module.comercializacao.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AnimalParaComercializacaoDto {
    @NotNull(message = "é obrigatorio informar o animal")
    private Integer animalReferencia;
    private Integer sequencial;
    private Double valorSugerido;
    private Double valorCambio;
}

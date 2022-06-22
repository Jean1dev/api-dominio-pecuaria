package com.binno.dominio.module.comercializacao.http.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class EnviarParaComercializacaoDto implements Serializable {
    private Integer animalReferencia;
    private Integer sequencial;
    private Double valorSugerido;
    private Double valorCambio;
    private Integer tenantId;
}

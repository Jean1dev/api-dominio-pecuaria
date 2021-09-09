package com.binno.dominio.module.vacinacao.api.dto;

import lombok.Data;

import java.util.List;

@Data
public final class ProcessarVacinacaoDto {

    private final List<Integer> animaisId;

    private final Integer medicamentoId;
}

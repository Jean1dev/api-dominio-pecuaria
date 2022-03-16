package com.binno.dominio.module.fazenda.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class QuantidadeAnimalPorFazendaDto {
    private Integer idFazenda;
    private String nomeFazenda;
    private Integer capacidadeMaximaFazenda;
    private long totalAnimais;
}

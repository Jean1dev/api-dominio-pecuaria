package com.binno.dominio.module.animal.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public final class TransferirAnimalEntreFazendaDto {

    private final Integer fazendaOrigemId;
    private final Integer fazendaDestinoId;
    private Boolean todosOsAnimais = false;
    private final List<Integer> animaisSelecionadosId;
}

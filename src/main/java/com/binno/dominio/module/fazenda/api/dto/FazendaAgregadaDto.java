package com.binno.dominio.module.fazenda.api.dto;

import com.binno.dominio.module.fazenda.model.Fazenda;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@Getter
public final class FazendaAgregadaDto implements Serializable {

    private final Integer id;
    private final String nome;

    public static FazendaAgregadaDto toDto(Fazenda fazenda) {
        if (Objects.isNull(fazenda))
            return null;

        return FazendaAgregadaDto.builder()
                .id(fazenda.getId())
                .nome(fazenda.getNome())
                .build();
    }
}

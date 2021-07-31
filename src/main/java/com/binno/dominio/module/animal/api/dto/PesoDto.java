package com.binno.dominio.module.animal.api.dto;

import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.model.PesoAnimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public final class PesoDto {
    private final Integer id;
    private final LocalDate dataPesagem;
    private final float peso;

    public static List<PesoDto> listToDto(List<PesoAnimal> pesoAnimal) {
        return pesoAnimal.stream()
                .map(PesoDto::toDto)
                .collect(Collectors.toList());
    }

    private static PesoDto toDto(PesoAnimal pesoAnimal) {
        return PesoDto.builder()
                .id(pesoAnimal.getId())
                .dataPesagem(pesoAnimal.getDataPesagem())
                .peso(pesoAnimal.getPeso())
                .build();
    }
}

package com.binno.dominio.module.animal.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public final class AssociarImagemNoAnimalDto {

    private final Integer animalId;
    private final String imagemUrl;
}

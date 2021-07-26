package com.binno.dominio.module.animal.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AssociarImagemNoAnimalDto {

    private Integer animalId;
    private String imagemUrl;
}

package com.binno.dominio.module.animal.api.dto;

import com.binno.dominio.module.imagem.model.Imagem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public final class ImagemAnimalDto {

    private final Integer id;
    private final Integer animalId;
    private final String imagemUrl;

    public static List<ImagemAnimalDto> listToDto(List<Imagem> imagem) {
        return imagem.stream()
                .map(ImagemAnimalDto::toDto)
                .collect(Collectors.toList());
    }

    private static ImagemAnimalDto toDto(Imagem imagem) {
        return ImagemAnimalDto.builder()
                .id(imagem.getId())
                .animalId(imagem.getReferenciaAnimal().getId())
                .imagemUrl(imagem.getUrl())
                .build();
    }
}

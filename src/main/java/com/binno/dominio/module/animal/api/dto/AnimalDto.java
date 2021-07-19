package com.binno.dominio.module.animal.api.dto;

import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.model.EstadoAtual;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public final class AnimalDto {
    private final Integer id;
    @NotNull(message = "O número não pode ser nulo ou vazio.")
    private final Integer numero;
    private final String raca;
    private final String apelido;
    private final LocalDate dataNascimento;
    private final Integer numeroCria;
    private final EstadoAtual estadoAtual;
    private final LocalDate dataUltimoParto;
    private final Boolean descarteFuturo;
    private final String justificativaDescarteFuturo;

    public static Page<AnimalDto> pageToDto(Page<Animal> animalPage) {
        List<AnimalDto> list = listToDto(animalPage.getContent());
        return new PageImpl<>(list, animalPage.getPageable(), animalPage.getTotalElements());
    }

    public static List<AnimalDto> listToDto(List<Animal> animais) {
        return animais.stream()
                .map(AnimalDto::toDto)
                .collect(Collectors.toList());
    }

    public static AnimalDto toDto(Animal animal) {
        return AnimalDto.builder()
                .id(animal.getId())
                .numero(animal.getNumero())
                .raca(animal.getRaca())
                .apelido(animal.getApelido())
                .dataNascimento(animal.getDataNascimento())
                .numeroCria(animal.getNumeroCrias())
                .estadoAtual(animal.getEstadoAtual())
                .dataUltimoParto(animal.getDataUltimoParto())
                .descarteFuturo(animal.getDescarteFuturo())
                .justificativaDescarteFuturo(animal.getJustificativaDescarteFuturo())
                .build();
    }
}

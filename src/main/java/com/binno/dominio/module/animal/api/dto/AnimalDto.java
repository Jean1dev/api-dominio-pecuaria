package com.binno.dominio.module.animal.api.dto;

import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.model.EstadoAtual;
import com.binno.dominio.module.tenant.model.Tenant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimalDto {
    private Integer id;
    @NotNull(message = "O número não pode ser nulo ou vazio.")
    private Integer numero;
    private String raca;
    private String apelido;
    private LocalDate dataNascimento;
    private Integer numeroCria;
    private EstadoAtual estadoAtual;
    private LocalDate dataUltimoParto;
    private Boolean descarteFuturo;
    private String justificativaDescarteFuturo;

    public static Page<AnimalDto> pageToDto(Page<Animal> animalPage) {
        List<AnimalDto> list = listToDto(animalPage.getContent());
        return new PageImpl<>(list, animalPage.getPageable(), list.size());
    }

    public static List<AnimalDto> listToDto(List<Animal> veiculos) {
        return veiculos.stream()
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
                .numeroCria(animal.getNumeroCria())
                .estadoAtual(animal.getEstadoAtual())
                .dataUltimoParto(animal.getDataUltimoParto())
                .descarteFuturo(animal.getDescarteFuturo())
                .justificativaDescarteFuturo(animal.getJustificativaDescarteFuturo())
                .build();
    }
}

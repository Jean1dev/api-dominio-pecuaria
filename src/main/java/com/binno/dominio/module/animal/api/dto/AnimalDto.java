package com.binno.dominio.module.animal.api.dto;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.model.EstadoAtual;
import com.binno.dominio.module.animal.model.RacaAnimal;
import com.binno.dominio.module.fazenda.api.dto.FazendaAgregadaDto;
import com.binno.dominio.module.tenant.model.Tenant;
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
    @NotNull(message = "{animal.numero.notnull}")
    private final Integer numero;
    private final RacaAnimal raca;
    private final String apelido;
    private final LocalDate dataNascimento;
    private final Integer numeroCria;
    private final EstadoAtual estadoAtual;
    private final LocalDate dataUltimoParto;
    private final Boolean descarteFuturo;
    private final Boolean isFemea;
    private final String justificativaDescarteFuturo;

    @NotNull(message = "{fazenda.fazenda.notnull}")
    private final FazendaAgregadaDto fazenda;
    private final List<PesoDto> pesos;
    private final List<ImagemAnimalDto> imagens;

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
                .isFemea(animal.getIsFemea())
                .justificativaDescarteFuturo(animal.getJustificativaDescarteFuturo())
                .fazenda(FazendaAgregadaDto.toDto(animal.getFazenda()))
                .pesos(PesoDto.listToDto(animal.getPesoAnimal()))
                .imagens(ImagemAnimalDto.listToDto(animal.getImagens()))
                .build();
    }

    public static Animal DtoToAnimal(CriarAnimalDto criarAnimalsDto, AuthenticationHolder holder) {
        return Animal.builder()
                .id(criarAnimalsDto.getId())
                .numero(criarAnimalsDto.getNumero())
                .raca(criarAnimalsDto.getRaca())
                .apelido(criarAnimalsDto.getApelido())
                .dataNascimento(criarAnimalsDto.getDataNascimento())
                .numeroCrias(criarAnimalsDto.getNumeroCrias())
                .estadoAtual(criarAnimalsDto.getEstadoAtual())
                .dataUltimoParto(criarAnimalsDto.getDataUltimoParto())
                .descarteFuturo(criarAnimalsDto.getDescarteFuturo())
                .isFemea(criarAnimalsDto.getIsFemea())
                .justificativaDescarteFuturo(criarAnimalsDto.getJustificativaDescarteFuturo())
                .fazenda(criarAnimalsDto.getFazenda())
                .tenant(Tenant.of(holder.getTenantId()))
                .build();
    }
}

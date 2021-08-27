package com.binno.dominio.module.animal.api.dto;

import com.binno.dominio.module.animal.model.EstadoAtual;
import com.binno.dominio.module.animal.model.RacaAnimal;
import com.binno.dominio.module.fazenda.model.Fazenda;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Getter
@AllArgsConstructor
@Builder
public final class CriarAnimalDto {
    private final Integer id;
    @NotNull(message = "{animal.numero.notnull}")
    private final Integer numero;
    private final RacaAnimal raca;
    private final String apelido;
    private final LocalDate dataNascimento;
    private final Integer numeroCrias;
    private final EstadoAtual estadoAtual;
    private final LocalDate dataUltimoParto;
    private final Boolean descarteFuturo;
    private final Boolean isFemea;
    private final String justificativaDescarteFuturo;
    @NotNull(message = "{fazenda.fazenda.notnull}")
    private final Fazenda fazenda;
    private final LocalDate dataPesagem;
    private final Integer idadeEmDias;
    private final Set<ImagemAnimalDto> imagens;
    private final Set<PesoDto> pesos;
}

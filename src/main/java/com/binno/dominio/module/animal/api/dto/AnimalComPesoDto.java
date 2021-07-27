package com.binno.dominio.module.animal.api.dto;

import com.binno.dominio.module.animal.model.EstadoAtual;
import com.binno.dominio.module.fazenda.model.Fazenda;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public final class AnimalComPesoDto {
    @NotNull(message = "O número não pode ser nulo ou vazio.")
    private final Integer numero;
    private final String raca;
    private final String apelido;
    private final LocalDate dataNascimento;
    private final Integer numeroCrias;
    private final EstadoAtual estadoAtual;
    private final LocalDate dataUltimoParto;
    private final Boolean descarteFuturo;
    private final Boolean isFemea;
    private final String justificativaDescarteFuturo;
    private final Fazenda fazenda;
    private LocalDate dataPesagem;
    private Integer peso;
    private Integer idadeEmDias;
}

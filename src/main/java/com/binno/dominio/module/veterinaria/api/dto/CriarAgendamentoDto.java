package com.binno.dominio.module.veterinaria.api.dto;

import com.binno.dominio.module.veterinaria.model.PeriodoDia;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public final class CriarAgendamentoDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate dataAgendamento;

    @NotNull
    private PeriodoDia periodoDia;

    private Integer veterinarioId;

    private Set<Integer> medicamentosId;
}

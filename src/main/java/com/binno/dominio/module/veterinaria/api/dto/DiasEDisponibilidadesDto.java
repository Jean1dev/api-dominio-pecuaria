package com.binno.dominio.module.veterinaria.api.dto;

import com.binno.dominio.module.veterinaria.model.PeriodoDia;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiasEDisponibilidadesDto {

    private Integer diaNumero;
    private Integer mesNumero;
    private PeriodoDia periodoDia;
    private Boolean disponivel;
}

package com.binno.dominio.module.veterinaria.api.dto;

import com.binno.dominio.module.veterinaria.model.AgendamentoVeterinario;
import com.binno.dominio.module.veterinaria.model.PeriodoDia;
import com.binno.dominio.module.veterinaria.model.StatusAgendamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public final class AgendamentoVeterinarioDto {

    private Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataSolicitacao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataAgendamento;
    private PeriodoDia periodoDia;
    private String observacoesVeterinario;
    private StatusAgendamento statusAgendamento;

    public static Page<AgendamentoVeterinarioDto> pageToDto(Page<AgendamentoVeterinario> page) {
        List<AgendamentoVeterinarioDto> dtos = listToDto(page.getContent());
        return new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
    }

    public static List<AgendamentoVeterinarioDto> listToDto(List<AgendamentoVeterinario> list) {
        return list.stream().map(AgendamentoVeterinarioDto::toDto).collect(Collectors.toList());
    }

    public static AgendamentoVeterinarioDto toDto(AgendamentoVeterinario entity) {
        return AgendamentoVeterinarioDto.builder()
                .id(entity.getId())
                .dataAgendamento(entity.getDataAgendamento())
                .observacoesVeterinario(entity.getObservacoesVeterinario())
                .statusAgendamento(entity.getStatusAgendamento())
                .dataSolicitacao(entity.getDataSolicitacao())
                .periodoDia(entity.getPeriodoDia())
                .build();
    }

}

package com.binno.dominio.module.medicamento.api.dto;

import com.binno.dominio.module.medicamento.model.Medicamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicamentoAgregadoDto {
    private Integer id;
    private String nome;

    public static MedicamentoAgregadoDto toDto(Medicamento medicamento) {
        if (Objects.isNull(medicamento))
            return null;

        return MedicamentoAgregadoDto.builder()
                .id(medicamento.getId())
                .nome(medicamento.getNome())
                .build();
    }
}

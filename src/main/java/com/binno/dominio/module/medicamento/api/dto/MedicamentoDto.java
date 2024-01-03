package com.binno.dominio.module.medicamento.api.dto;

import com.binno.dominio.module.medicamento.model.EstadoMedicamento;
import com.binno.dominio.module.medicamento.model.Medicamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicamentoDto {
    private Integer id;
    private String nome;
    @NotNull(message = "O nome não pode ser nulo ou vazio.")
    private String descricao;
    private LocalDate dataValidade;
    private EstadoMedicamento estadoMedicamento;

    public static Page<MedicamentoDto> pageToDto(Page<Medicamento> medicamentoPage) {
        List<MedicamentoDto> list = listToDto(medicamentoPage.getContent());
        return new PageImpl<>(list, medicamentoPage.getPageable(), medicamentoPage.getTotalElements());
    }

    public static List<MedicamentoDto> listToDto(List<Medicamento> medicamentos) {
        return medicamentos.stream()
                .map(MedicamentoDto::toDto)
                .collect(Collectors.toList());
    }

    public static MedicamentoDto toDto(Medicamento medicamento) {
        return MedicamentoDto.builder()
                .id(medicamento.getId())
                .nome(medicamento.getNome())
                .descricao(medicamento.getDescricao())
                .dataValidade(medicamento.getDataValidade())
                .estadoMedicamento(medicamento.getEstadoMedicamento())
                .build();
    }
}

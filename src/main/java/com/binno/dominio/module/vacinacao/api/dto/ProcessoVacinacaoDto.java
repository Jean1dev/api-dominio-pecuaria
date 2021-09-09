package com.binno.dominio.module.vacinacao.api.dto;

import com.binno.dominio.module.medicamento.model.Medicamento;
import com.binno.dominio.module.vacinacao.model.ProcessoVacinacao;
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
public final class ProcessoVacinacaoDto {

    private final Integer id;
    private Boolean processoRevertido;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataProcesso;
    private Integer totalAnimaisVacinados;
    private String animaisIdSeparadoPorVirgula;
    private MedicamentoAgregadoDto medicamento;

    public static Page<ProcessoVacinacaoDto> pageToDto(Page<ProcessoVacinacao> page) {
        List<ProcessoVacinacaoDto> list = listToDto(page.getContent());
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }

    public static List<ProcessoVacinacaoDto> listToDto(List<ProcessoVacinacao> list) {
        return list.stream().map(ProcessoVacinacaoDto::toDto).collect(Collectors.toList());
    }

    public static ProcessoVacinacaoDto toDto(ProcessoVacinacao processoVacinacao) {
        return ProcessoVacinacaoDto.builder()
                .id(processoVacinacao.getId())
                .processoRevertido(processoVacinacao.getProcessoRevertido())
                .dataProcesso(processoVacinacao.getDataProcesso())
                .totalAnimaisVacinados(processoVacinacao.getTotalAnimaisVacinados())
                .medicamento(MedicamentoAgregadoDto.toDto(processoVacinacao.getMedicamento()))
                .build();
    }

    @Data
    @Builder
    private static final class MedicamentoAgregadoDto {
        private final Integer id;
        private final String descricao;

        public static MedicamentoAgregadoDto toDto(Medicamento medicamento) {
            return MedicamentoAgregadoDto.builder()
                    .id(medicamento.getId())
                    .descricao(medicamento.getNome())
                    .build();
        }
    }
}

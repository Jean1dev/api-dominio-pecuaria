package com.binno.dominio.module.fazenda.api.dto;

import com.binno.dominio.module.fazenda.model.Fazenda;
import com.binno.dominio.module.fazenda.model.TipoMetragem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public final class FazendaDto {
    private final Integer id;
    @NotNull(message = "O nome não pode ser nulo ou vazio.")
    private final String nome;
    private final Integer codigoEstab;
    private final String endereco;
    private final Integer metragem;
    private final TipoMetragem tipoMetragem;
    private final Integer capacidadeMaxGado;

    public static Page<FazendaDto> pageToDto(Page<Fazenda> fazendaPage) {
        List<FazendaDto> list = listToDto(fazendaPage.getContent());
        return new PageImpl<>(list, fazendaPage.getPageable(), fazendaPage.getTotalElements());
    }

    public static List<FazendaDto> listToDto(List<Fazenda> fazendas) {
        return fazendas.stream()
                .map(FazendaDto::toDto)
                .collect(Collectors.toList());
    }

    public static FazendaDto toDto(Fazenda fazenda) {
        return FazendaDto.builder()
                .id(fazenda.getId())
                .nome(fazenda.getNome())
                .codigoEstab(fazenda.getCodEstab())
                .endereco(fazenda.getEndereco())
                .metragem(fazenda.getMetragem())
                .tipoMetragem(fazenda.getTipoMetragem())
                .capacidadeMaxGado(fazenda.getCapMaximaGado())
                .build();
    }
}

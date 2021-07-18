package com.binno.dominio.module.fazenda.api.dto;

import com.binno.dominio.module.fazenda.model.Fazenda;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public final class FazendaDto {
    private final Integer id;
    private final String nome;
    private final Integer codigoEstab;
    private final String endereco;
    private final Integer tamanhoHectare;
    private final Integer capacidadeMaxGado;

    public static Page<FazendaDto> pageToDto(Page<Fazenda> fazendaPage) {
        List<FazendaDto> list = listToDto(fazendaPage.getContent());
        return new PageImpl<>(list, fazendaPage.getPageable(), list.size());
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
                .tamanhoHectare(fazenda.getMetragem())
                .capacidadeMaxGado(fazenda.getCapMaximaGado())
                .build();
    }
}

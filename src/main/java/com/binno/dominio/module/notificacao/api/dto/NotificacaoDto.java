package com.binno.dominio.module.notificacao.api.dto;

import com.binno.dominio.module.notificacao.model.Notificacao;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class NotificacaoDto {

    private Integer id;
    private String descricao;

    public static List<NotificacaoDto> listToDto(List<Notificacao> list) {
        return list.stream().map(NotificacaoDto::toDto).collect(Collectors.toList());
    }

    public static NotificacaoDto toDto(Notificacao notificacao) {
        return NotificacaoDto.builder()
                .id(notificacao.getId())
                .descricao(notificacao.getDescricao())
                .build();
    }
}

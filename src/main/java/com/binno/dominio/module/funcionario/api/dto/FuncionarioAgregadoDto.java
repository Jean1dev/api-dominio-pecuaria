package com.binno.dominio.module.funcionario.api.dto;

import com.binno.dominio.module.funcionario.model.Funcionario;
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
public final class FuncionarioAgregadoDto {
    private final Integer id;
    private final String nome;

    public static Page<FuncionarioAgregadoDto> pageToDto(Page<Funcionario> funcionarioPage) {
        List<FuncionarioAgregadoDto> list = listToDto(funcionarioPage.getContent());
        return new PageImpl<>(list, funcionarioPage.getPageable(), list.size());
    }

    public static List<FuncionarioAgregadoDto> listToDto(List<Funcionario> funcionarios) {
        return funcionarios.stream()
                .map(FuncionarioAgregadoDto::toDto)
                .collect(Collectors.toList());
    }

    public static FuncionarioAgregadoDto toDto(Funcionario funcionario) {
        return FuncionarioAgregadoDto.builder()
                .id(funcionario.getId())
                .nome(funcionario.getNome())
                .build();
    }
}

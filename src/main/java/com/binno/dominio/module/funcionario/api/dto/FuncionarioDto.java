package com.binno.dominio.module.funcionario.api.dto;

import com.binno.dominio.module.fazenda.api.dto.FazendaAgregadaDto;
import com.binno.dominio.module.funcionario.model.Funcionario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public final class FuncionarioDto {
    private final Integer id;
    private final String nome;
    private final String cargo;
    private final String rg;
    private final String cpf;
    private final FazendaAgregadaDto fazenda;

    public static Page<FuncionarioDto> pageToDto(Page<Funcionario> funcionarioPage) {
        List<FuncionarioDto> list = listToDto(funcionarioPage.getContent());
        return new PageImpl<>(list, funcionarioPage.getPageable(), list.size());
    }

    public static List<FuncionarioDto> listToDto(List<Funcionario> funcionarios) {
        return funcionarios.stream()
                .map(FuncionarioDto::toDto)
                .collect(Collectors.toList());
    }

    public static FuncionarioDto toDto(Funcionario funcionario) {
        return FuncionarioDto.builder()
                .id(funcionario.getId())
                .nome(funcionario.getNome())
                .cargo(funcionario.getCargo())
                .rg(funcionario.getRg())
                .cpf(funcionario.getCpf())
                .fazenda(FazendaAgregadaDto.toDto(funcionario.getFazenda()))
                .build();
    }
}

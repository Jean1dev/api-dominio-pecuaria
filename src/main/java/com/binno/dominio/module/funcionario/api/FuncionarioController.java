package com.binno.dominio.module.funcionario.api;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.fazenda.model.Fazenda;
import com.binno.dominio.module.funcionario.api.dto.FuncionarioDto;
import com.binno.dominio.module.funcionario.model.Funcionario;
import com.binno.dominio.module.funcionario.repository.FuncionarioRepository;
import com.binno.dominio.module.tenant.model.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.binno.dominio.module.funcionario.api.dto.FuncionarioDto.pageToDto;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioRepository repository;

    @Autowired
    private AuthenticationHolder holder;

    @GetMapping
    public Page<FuncionarioDto> funcionariosPaginated(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                      @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return pageToDto(repository.findAllByTenantId(PageRequest.of(page, size), holder.getTenantId()));
    }

    @PostMapping
    public Funcionario create(@RequestBody @Valid FuncionarioDto funcionarioDto) {
        return repository.save(Funcionario.builder()
                .nome(funcionarioDto.getNome())
                .cargo(funcionarioDto.getCargo())
                .rg(funcionarioDto.getRg())
                .cpf(funcionarioDto.getCpf())
                .fazenda(Fazenda.builder().id(funcionarioDto.getFazenda().getId()).build())
                .tenant(Tenant.builder().id(holder.getTenantId()).build())
                .build());
    }
}

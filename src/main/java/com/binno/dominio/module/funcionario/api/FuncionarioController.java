package com.binno.dominio.module.funcionario.api;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.fazenda.model.Fazenda;
import com.binno.dominio.module.funcionario.api.dto.FuncionarioDto;
import com.binno.dominio.module.funcionario.model.Funcionario;
import com.binno.dominio.module.funcionario.repository.FuncionarioRepository;
import com.binno.dominio.module.tenant.model.Tenant;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.binno.dominio.module.funcionario.api.dto.FuncionarioDto.pageToDto;
import static com.binno.dominio.module.funcionario.api.dto.FuncionarioDto.toDto;
import static com.binno.dominio.module.funcionario.specification.FuncionarioSpecification.nome;
import static com.binno.dominio.module.funcionario.specification.FuncionarioSpecification.tenant;

@Slf4j
@RestController
@RequestMapping(FuncionarioController.PATH)
@RequiredArgsConstructor
public class FuncionarioController {

    public static final String PATH = "funcionarios";

    private final FuncionarioRepository repository;

    private final AuthenticationHolder holder;

    @GetMapping
    public Page<FuncionarioDto> funcionariosPaginated(
            @RequestParam(value = "nome", defaultValue = "", required = false) String nome,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return pageToDto(repository.findAll(Specification.where(tenant(holder.getTenantId()).and(nome(nome))), PageRequest.of(page, size)));
    }

    @GetMapping(path = "/{id}")
    public FuncionarioDto getOne(@PathVariable Integer id) {
        return toDto(repository.findById(id).orElseThrow());
    }

    @PostMapping
    @Operation( description = "Criar um funcionario")
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

    @PutMapping
    @Operation( description = "Atualizar Funcionario")
    public Funcionario update(@RequestBody @Valid FuncionarioDto funcionarioDto) {
        repository.findById(funcionarioDto.getId()).orElseThrow();
        return repository.save(Funcionario.builder()
                .id(funcionarioDto.getId())
                .nome(funcionarioDto.getNome())
                .cargo(funcionarioDto.getCargo())
                .rg(funcionarioDto.getRg())
                .cpf(funcionarioDto.getCpf())
                .fazenda(Fazenda.builder().id(funcionarioDto.getFazenda().getId()).build())
                .tenant(Tenant.builder().id(holder.getTenantId()).build())
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation( description = "Excluir funcionario")
    public void delete(@PathVariable("id") Integer id) {
        repository.deleteById(id);
    }
}

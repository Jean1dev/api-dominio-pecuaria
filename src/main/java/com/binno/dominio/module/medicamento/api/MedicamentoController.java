package com.binno.dominio.module.medicamento.api;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.medicamento.api.dto.MedicamentoDto;
import com.binno.dominio.module.medicamento.model.Medicamento;
import com.binno.dominio.module.medicamento.repository.MedicamentoRepository;
import com.binno.dominio.module.tenant.model.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.binno.dominio.module.medicamento.api.dto.MedicamentoDto.pageToDto;

@RestController
@RequestMapping(MedicamentoController.PATH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicamentoController {

    public static final String PATH = "medicamentos";

    private final MedicamentoRepository repository;

    private final AuthenticationHolder holder;

    @GetMapping
    public Page<MedicamentoDto> medicamentosPaginated(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                      @RequestParam(value = "size", defaultValue = "10", required = false) int size) {

        return pageToDto(repository.findAllByTenantId(PageRequest.of(page, size), holder.getTenantId()));
    }

    @PostMapping
    public Medicamento create(@RequestBody @Valid MedicamentoDto medicamentoDto) {
        return repository.save(Medicamento.builder()
                .nome(medicamentoDto.getNome())
                .descricao(medicamentoDto.getDescricao())
                .dataValidade(medicamentoDto.getDataValidade())
                .tenant(Tenant.builder().id(holder.getTenantId()).build())
                .build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        repository.deleteById(id);
    }
}

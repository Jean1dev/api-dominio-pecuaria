package com.binno.dominio.module.medicamento.api;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.medicamento.api.dto.MedicamentoDto;
import com.binno.dominio.module.medicamento.model.Medicamento;
import com.binno.dominio.module.medicamento.repository.MedicamentoRepository;
import com.binno.dominio.module.tenant.model.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import static com.binno.dominio.module.medicamento.api.dto.MedicamentoDto.pageToDto;

@RestController
@RequestMapping("/medicamentos")
public class MedicamentoController {

    @Autowired
    private MedicamentoRepository repository;

    @Autowired
    private AuthenticationHolder holder;

    @GetMapping
    public Page<MedicamentoDto> medicamentosPaginated(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                      @RequestParam(value = "size", defaultValue = "10", required = false) int size) {

        return pageToDto(repository.findAllByTenantId(PageRequest.of(page, size), holder.getTenantId()));
    }

    @PostMapping
    public Medicamento create(@RequestBody MedicamentoDto medicamentoDto) {
        return repository.save(Medicamento.builder()
                .nome(medicamentoDto.getNome())
                .descricao(medicamentoDto.getDescricao())
                .dataValidade(medicamentoDto.getDataValidade())
                .tenant(Tenant.builder().id(holder.getTenantId()).build())
                .build());
    }
}

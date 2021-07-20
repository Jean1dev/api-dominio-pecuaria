package com.binno.dominio.module.fazenda.api;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.fazenda.api.dto.FazendaAgregadaDto;
import com.binno.dominio.module.fazenda.api.dto.FazendaDto;
import com.binno.dominio.module.fazenda.model.Fazenda;
import com.binno.dominio.module.fazenda.repository.FazendaRepository;
import com.binno.dominio.module.tenant.model.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.binno.dominio.module.fazenda.api.dto.FazendaAgregadaDto.listToDtoAgregado;
import static com.binno.dominio.module.fazenda.api.dto.FazendaDto.pageToDto;

@RestController
@RequestMapping("/fazendas")
public class FazendaController {

    @Autowired
    private FazendaRepository repository;

    @Autowired
    private AuthenticationHolder holder;

    @GetMapping(path = "listagem")
    public List<FazendaAgregadaDto> listagemSimplificada() {
        return listToDtoAgregado(repository.findAllByTenantId(holder.getTenantId()));
    }

    @GetMapping
    public Page<FazendaDto> fazendasPaginated(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                              @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return pageToDto(repository.findAllByTenantId(PageRequest.of(page, size), holder.getTenantId()));
    }

    @PostMapping
    public Fazenda create(@RequestBody @Valid FazendaDto fazendaDto) {
        return repository.save(Fazenda.builder()
                .nome(fazendaDto.getNome())
                .codEstab(fazendaDto.getCodigoEstab())
                .endereco(fazendaDto.getEndereco())
                .metragem(fazendaDto.getTamanhoHectare())
                .capMaximaGado(fazendaDto.getCapacidadeMaxGado())
                .tenant(Tenant.builder().id(holder.getTenantId()).build())
                .build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        repository.deleteById(id);
    }
}

package com.binno.dominio.module.fazenda.api;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.fazenda.api.dto.FazendaAgregadaDto;
import com.binno.dominio.module.fazenda.api.dto.FazendaDto;
import com.binno.dominio.module.fazenda.model.Fazenda;
import com.binno.dominio.module.fazenda.repository.FazendaRepository;
import com.binno.dominio.module.tenant.model.Tenant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.binno.dominio.module.fazenda.api.dto.FazendaAgregadaDto.listToDtoAgregado;
import static com.binno.dominio.module.fazenda.api.dto.FazendaDto.pageToDto;
import static com.binno.dominio.module.fazenda.specification.FazendaSpecification.nome;
import static com.binno.dominio.module.fazenda.specification.FazendaSpecification.tenant;

@RestController
@Slf4j
@RequestMapping(FazendaController.PATH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FazendaController {

    public static final String PATH = "fazendas";

    private final FazendaRepository repository;

    private final AuthenticationHolder holder;

    @GetMapping(path = "listagem")
    public List<FazendaAgregadaDto> listagemSimplificada() {
        return listToDtoAgregado(repository.findAllByTenantId(holder.getTenantId()));
    }

    @GetMapping
    public Page<FazendaDto> fazendasPaginated(
            @RequestParam(value = "nome", defaultValue = "", required = false) String nome,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return pageToDto(repository.findAll(Specification.where(tenant(holder.getTenantId()).and(nome(nome))), PageRequest.of(page, size)));
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
    public void delete(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}

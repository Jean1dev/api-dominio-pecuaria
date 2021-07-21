package com.binno.dominio.module.animal.api;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.api.dto.AnimalDto;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.tenant.model.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.binno.dominio.module.animal.api.dto.AnimalDto.pageToDto;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    @Autowired
    private AnimalRepository repository;

    @Autowired
    private AuthenticationHolder holder;

    @GetMapping
    public Page<AnimalDto> animaisPaginated(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return pageToDto(repository.findAllByTenantId(PageRequest.of(page, size), holder.getTenantId()));
    }

    @PostMapping
    public Animal create(@RequestBody @Valid AnimalDto animalDto) {
        return repository.save(Animal.builder()
                .numero(animalDto.getNumero())
                .raca(animalDto.getRaca())
                .apelido(animalDto.getApelido())
                .dataNascimento(animalDto.getDataNascimento())
                .numeroCrias(animalDto.getNumeroCria())
                .estadoAtual(animalDto.getEstadoAtual())
                .dataUltimoParto(animalDto.getDataUltimoParto())
                .descarteFuturo(animalDto.getDescarteFuturo())
                .justificativaDescarteFuturo(animalDto.getJustificativaDescarteFuturo())
                .isFemea(animalDto.getIsFemea())
                .tenant(Tenant.builder().id(holder.getTenantId()).build())
                .build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        repository.deleteById(id);
    }
}

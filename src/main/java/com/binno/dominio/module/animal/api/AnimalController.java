package com.binno.dominio.module.animal.api;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.api.dto.AnimalDto;
import com.binno.dominio.module.animal.api.dto.AssociarImagemNoAnimalDto;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.animal.service.AssociarImagem;
import com.binno.dominio.module.fazenda.model.Fazenda;
import com.binno.dominio.module.tenant.model.Tenant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.binno.dominio.module.animal.api.dto.AnimalDto.pageToDto;

@RestController
@RequestMapping("/animais")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnimalController {

    private final AnimalRepository repository;

    private final AuthenticationHolder holder;

    private final AssociarImagem associarImagemService;

    @GetMapping
    public Page<AnimalDto> animaisPaginated(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return pageToDto(repository.findAllByTenantId(PageRequest.of(page, size), holder.getTenantId()));
    }

    @PostMapping(path = "adicionar-imagem")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void linkarImagem(@RequestBody AssociarImagemNoAnimalDto dto) {
        associarImagemService.executar(dto);
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
                .fazenda(animalDto.getFazenda())
                .fazenda(Fazenda.builder().id(animalDto.getFazenda().getId()).build())
                .tenant(Tenant.builder().id(holder.getTenantId()).build())
                .build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        repository.deleteById(id);
    }
}

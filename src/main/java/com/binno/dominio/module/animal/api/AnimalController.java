package com.binno.dominio.module.animal.api;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.api.dto.CriarAnimalDto;
import com.binno.dominio.module.animal.api.dto.AnimalDto;
import com.binno.dominio.module.animal.api.dto.AssociarImagemNoAnimalDto;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.animal.service.CriarAnimalService;
import com.binno.dominio.module.animal.service.AssociarImagemService;
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

    private final AssociarImagemService associarImagemService;

    private final CriarAnimalService service;

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
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid CriarAnimalDto criarAnimalDto) {
        service.executar(criarAnimalDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        repository.deleteById(id);
    }
};

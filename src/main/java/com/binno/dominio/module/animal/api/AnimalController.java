package com.binno.dominio.module.animal.api;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.api.dto.AnimalComPesoDto;
import com.binno.dominio.module.animal.api.dto.AnimalDto;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.animal.service.CriarAnimalService;
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

    @Autowired
    private CriarAnimalService service;

    @GetMapping
    public Page<AnimalDto> animaisPaginated(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return pageToDto(repository.findAllByTenantId(PageRequest.of(page, size), holder.getTenantId()));
    }

    @PostMapping
    public Animal create(@RequestBody @Valid AnimalComPesoDto animalComPesoDto) {
        return service.executar(animalComPesoDto);
    }

//    @PostMapping
//    public Animal insert(@RequestBody @Valid AnimalDto animalDto) {
//        return repository.save(Animal.builder()
//                .build());
//    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        repository.deleteById(id);
    }
};
package com.binno.dominio.module.animal.api;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.api.dto.AnimalDto;
import com.binno.dominio.module.animal.api.dto.AssociarImagemNoAnimalDto;
import com.binno.dominio.module.animal.api.dto.CriarAnimalDto;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.animal.service.AnimalConsultasService;
import com.binno.dominio.module.animal.service.AssociarImagemService;
import com.binno.dominio.module.animal.service.CriarAnimalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;

import static com.binno.dominio.module.animal.api.dto.AnimalDto.pageToDto;
import static com.binno.dominio.module.animal.api.dto.AnimalDto.toDto;
import static com.binno.dominio.module.animal.specification.AnimalSpecification.numero;
import static com.binno.dominio.module.animal.specification.AnimalSpecification.tenant;

@RestController
@RequestMapping(AnimalController.PATH)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnimalController {

    public static final String PATH = "animais";

    private final AnimalRepository repository;
    private final AuthenticationHolder holder;
    private final AssociarImagemService associarImagemService;
    private final CriarAnimalService service;
    private final AnimalConsultasService consultasService;

    @GetMapping(path = "total-por-sexo")
    public Map totalPorSexo(@RequestParam(value = "femea", required = false, defaultValue = "false") Boolean isFemea) {
        long totalPorSexo = consultasService.totalPorSexo(isFemea);
        long total = consultasService.total();
        BigDecimal porcetagem = BigDecimal.valueOf((double) totalPorSexo / (double) total)
                .setScale(2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
        return Map.of(
                "totalSolicitado", totalPorSexo,
                "total", total,
                "porcetagem", porcetagem
        );
    }

    @GetMapping
    public Page<AnimalDto> animaisPaginated(
            @RequestParam(value = "numero", required = false) Integer numero,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        Specification<Animal> specs = Specification.where(tenant(holder.getTenantId()));
        if (Objects.nonNull(numero))
            specs = specs.and(numero(numero));

        return pageToDto(repository.findAll(specs, PageRequest.of(page, size)));
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

    @GetMapping("/{id}")
    public AnimalDto getAnimalById(@PathVariable("id") Integer id) {
        return toDto(repository.findById(id).orElseThrow());
    }
}

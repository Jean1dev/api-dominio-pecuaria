package com.binno.dominio.module.animal.api;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.api.dto.AnimalDto;
import com.binno.dominio.module.animal.api.dto.CriarAnimalDto;
import com.binno.dominio.module.animal.api.dto.ImagemAnimalDto;
import com.binno.dominio.module.animal.api.dto.TransferirAnimalEntreFazendaDto;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.animal.service.AnimalConsultasService;
import com.binno.dominio.module.animal.service.AssociarImagemService;
import com.binno.dominio.module.animal.service.CriarAnimalService;
import com.binno.dominio.module.animal.service.TransferirAnimalEntreFazendasService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import static com.binno.dominio.module.animal.api.dto.AnimalDto.pageToDto;
import static com.binno.dominio.module.animal.api.dto.AnimalDto.toDto;

@RestController
@RequestMapping(AnimalController.PATH)
@Slf4j
@RequiredArgsConstructor
public class AnimalController {

    public static final String PATH = "animais";

    private final AnimalRepository repository;
    private final AuthenticationHolder holder;
    private final AssociarImagemService associarImagemService;
    private final CriarAnimalService service;
    private final AnimalConsultasService consultasService;
    private final TransferirAnimalEntreFazendasService entreFazendasService;

    @GetMapping(path = "total-por-sexo")
    public Map totalPorSexo(@RequestParam(value = "femea", required = false, defaultValue = "false") Boolean isFemea) {
        long totalPorSexo = consultasService.totalPorSexo(isFemea);
        long total = consultasService.total();
        BigDecimal porcetagem = BigDecimal.valueOf(0);
        if (totalPorSexo != 0L && total != 0L) {
            porcetagem = BigDecimal.valueOf((double) totalPorSexo / (double) total)
                    .setScale(2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100));
        }

        return Map.of(
                "totalSolicitado", totalPorSexo,
                "total", total,
                "porcetagem", porcetagem
        );
    }

    @GetMapping
    @Operation(description = "Listagem de animais pageado e com filtros")
    public Page<AnimalDto> animaisPaginated(
            GetAllAnimalFilterRequest filter,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return pageToDto(repository.findAll(filter.buildSpecification(holder.getTenantId()), filter.buildSortAndPageable(page, size)));
    }

    @GetMapping(path = "ultimo-numero")
    @Operation(description = "Retorna o ultimo numero de animal cadastrado")
    public long getUltimoNumeroDoAnimalCadastrado() {
        return consultasService.getUltimoNumeroDoAnimalCadastrado();
    }

    @PostMapping(path = "adicionar-imagem")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(description = "Adiciona uma imagem no Animal")
    public void linkarImagem(@RequestBody ImagemAnimalDto dto) {
        associarImagemService.executar(dto);
    }

    @PostMapping(path = "transferencia-entre-fazendas")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void transferenciaDeAnimais(@RequestBody TransferirAnimalEntreFazendaDto dto) {
        entreFazendasService.executar(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid CriarAnimalDto criarAnimalDto) {
        service.executar(criarAnimalDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void update(@RequestBody @Valid CriarAnimalDto criarAnimalDto) {
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

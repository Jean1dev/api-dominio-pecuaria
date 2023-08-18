package com.binno.dominio.module.comercializacao.api;

import com.binno.dominio.module.comercializacao.api.dto.AnimalParaComercializacaoDto;
import com.binno.dominio.module.comercializacao.http.dto.CambioDto;
import com.binno.dominio.module.comercializacao.service.ComercializacaoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(ComercializacaoController.PATH)
@RequiredArgsConstructor
public class ComercializacaoController {

    public static final String PATH = "comercializacao";

    private final ComercializacaoService service;

    @Operation( description = "Retorna a ultima cotação registrada")
    @GetMapping(path = "ultima")
    public CambioDto getUltimaCotacao() {
        return service.getUltimaCotacao();
    }

    @Operation( description = "Minha lista de comercializacao")
    @GetMapping()
    public List<AnimalParaComercializacaoDto> getMinhaLista() {
        return service.recuperarListaDoUsuario();
    }

    @Operation( description = "Adiciona um animal na lista de comercializacao")
    @PostMapping
    public void adicionarAnimalNaComercializacao(@RequestBody @Valid AnimalParaComercializacaoDto dto) {
        service.adicionarAnimalNaComercializacao(dto);
    }

    @Operation( description = "Confirmar lista de comercializacao e enviar")
    @PostMapping(path = "confirmar")
    public void enviarParaComercializacao() {
        service.enviarParaComercializacao();
    }

    @Operation( description = "Remove o animal da lista de comercializacao")
    @PostMapping(path = "remove")
    public void removerAnimalNaComercializacao(@RequestBody @Valid AnimalParaComercializacaoDto dto) {
        service.removerAnimalNaComercializacao(dto);
    }
}

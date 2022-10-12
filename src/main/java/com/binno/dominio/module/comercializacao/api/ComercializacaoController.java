package com.binno.dominio.module.comercializacao.api;

import com.binno.dominio.module.comercializacao.api.dto.AnimalParaComercializacaoDto;
import com.binno.dominio.module.comercializacao.http.dto.CambioDto;
import com.binno.dominio.module.comercializacao.service.ComercializacaoService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(ComercializacaoController.PATH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ComercializacaoController {

    public static final String PATH = "comercializacao";

    private final ComercializacaoService service;

    @ApiOperation("Retorna a ultima cotação registrada")
    @GetMapping(path = "ultima")
    public CambioDto getUltimaCotacao() {
        return service.getUltimaCotacao();
    }

    @ApiOperation("Minha lista de comercializacao")
    @GetMapping()
    public List<AnimalParaComercializacaoDto> getMinhaLista() {
        return service.recuperarListaDoUsuario();
    }

    @ApiOperation("Adiciona um animal na lista de comercializacao")
    @PostMapping
    public void adicionarAnimalNaComercializacao(@RequestBody @Valid AnimalParaComercializacaoDto dto) {
        service.adicionarAnimalNaComercializacao(dto);
    }

    @ApiOperation("Confirmar lista de comercializacao e enviar")
    @PostMapping(path = "confirmar")
    public void enviarParaComercializacao() {
        service.enviarParaComercializacao();
    }

    @ApiOperation("Remove o animal da lista de comercializacao")
    @PostMapping(path = "remove")
    public void removerAnimalNaComercializacao(@RequestBody @Valid AnimalParaComercializacaoDto dto) {
        service.removerAnimalNaComercializacao(dto);
    }
}

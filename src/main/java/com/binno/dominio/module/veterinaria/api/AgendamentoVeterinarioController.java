package com.binno.dominio.module.veterinaria.api;

import com.binno.dominio.module.veterinaria.api.dto.CriarAgendamentoDto;
import com.binno.dominio.module.veterinaria.service.SolicitarAgendamentoVeterinario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(AgendamentoVeterinarioController.PATH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AgendamentoVeterinarioController {

    public static final String PATH = "agendamento-veterinario";

    private final SolicitarAgendamentoVeterinario solicitarAgendamentoVeterinario;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarAgendamento(@RequestBody @Valid CriarAgendamentoDto dto) {
        solicitarAgendamentoVeterinario.executar(dto);
    }
}

package com.binno.dominio.module.veterinaria.api;

import com.binno.dominio.module.veterinaria.api.dto.CriarAgendamentoDto;
import com.binno.dominio.module.veterinaria.api.dto.DiasEDisponibilidadesDto;
import com.binno.dominio.module.veterinaria.service.SolicitarAgendamentoVeterinario;
import com.binno.dominio.module.veterinaria.service.VerificarDisponibilidadeParaAgendamentos;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(AgendamentoVeterinarioController.PATH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AgendamentoVeterinarioController {

    public static final String PATH = "agendamento-veterinario";

    private final SolicitarAgendamentoVeterinario solicitarAgendamentoVeterinario;
    private final VerificarDisponibilidadeParaAgendamentos verificarDisponibilidadeParaAgendamentos;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarAgendamento(@RequestBody @Valid CriarAgendamentoDto dto) {
        solicitarAgendamentoVeterinario.executar(dto);
    }

    @GetMapping(path = "disponibilidade")
    public List<DiasEDisponibilidadesDto> consultarDisponibilidade(
            @RequestParam("dias") Integer dias,
            @RequestParam("data") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataPesquisa,
            @RequestParam(value = "exibir-dias-indisponiveis", required = false) Boolean exibirDiasIndisponiveis) {
        return verificarDisponibilidadeParaAgendamentos.executar(dias, dataPesquisa, exibirDiasIndisponiveis);
    }
}

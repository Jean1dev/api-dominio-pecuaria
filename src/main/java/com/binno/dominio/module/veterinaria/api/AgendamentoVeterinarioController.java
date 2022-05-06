package com.binno.dominio.module.veterinaria.api;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.veterinaria.api.dto.AgendamentoVeterinarioDto;
import com.binno.dominio.module.veterinaria.api.dto.CriarAgendamentoDto;
import com.binno.dominio.module.veterinaria.api.dto.DiasEDisponibilidadesDto;
import com.binno.dominio.module.veterinaria.repository.AgendamentoVeterinarioRepository;
import com.binno.dominio.module.veterinaria.service.SolicitarAgendamentoVeterinario;
import com.binno.dominio.module.veterinaria.service.VerificarDisponibilidadeParaAgendamentos;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static com.binno.dominio.module.veterinaria.api.dto.AgendamentoVeterinarioDto.pageToDto;

@RestController
@RequestMapping(AgendamentoVeterinarioController.PATH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AgendamentoVeterinarioController {

    public static final String PATH = "agendamento-veterinario";

    private final AuthenticationHolder holder;
    private final AgendamentoVeterinarioRepository repository;
    private final SolicitarAgendamentoVeterinario solicitarAgendamentoVeterinario;
    private final VerificarDisponibilidadeParaAgendamentos verificarDisponibilidadeParaAgendamentos;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Cria um novo agendamento")
    public void criarAgendamento(@RequestBody @Valid CriarAgendamentoDto dto) {
        solicitarAgendamentoVeterinario.executar(dto);
    }

    @GetMapping
    @ApiOperation("Retorna a lista de agendamentos do usuario")
    public Page<AgendamentoVeterinarioDto> findAll(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                   @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return pageToDto(repository.findAllByTenantId(PageRequest.of(page, size), holder.getTenantId()));
    }

    @GetMapping(path = "disponibilidade")
    @ApiOperation("Consulta a disponibilidade de dias para agendamento")
    public List<DiasEDisponibilidadesDto> consultarDisponibilidade(
            @RequestParam("dias") Integer dias,
            @RequestParam("data") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataPesquisa,
            @RequestParam(value = "exibir-dias-indisponiveis", required = false) Boolean exibirDiasIndisponiveis) {
        return verificarDisponibilidadeParaAgendamentos.executar(dias, dataPesquisa, exibirDiasIndisponiveis);
    }
}

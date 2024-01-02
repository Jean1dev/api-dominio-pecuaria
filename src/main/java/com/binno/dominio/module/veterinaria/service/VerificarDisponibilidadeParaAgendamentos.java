package com.binno.dominio.module.veterinaria.service;

import com.binno.dominio.module.veterinaria.api.dto.DiasEDisponibilidadesDto;
import com.binno.dominio.module.veterinaria.model.AgendamentoVeterinario;
import com.binno.dominio.module.veterinaria.model.PeriodoDia;
import com.binno.dominio.module.veterinaria.model.StatusAgendamento;
import com.binno.dominio.module.veterinaria.repository.AgendamentoVeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VerificarDisponibilidadeParaAgendamentos {

    private final AgendamentoVeterinarioRepository repository;

    public List<DiasEDisponibilidadesDto> executar(Integer dias, LocalDate dataPesquisa, Boolean exibirDiasIndisponiveis) {
        if (dias < 0)
            throw new ValidationException("O numero em dias precisa ser maior que zero");

        List<DiasEDisponibilidadesDto> disponibilidadesDtos = new ArrayList<>();
        LocalDate hoje = LocalDate.from(dataPesquisa);
        LocalDate dataFinalConsulta = LocalDate.now().plusDays(dias);
        List<AgendamentoVeterinario> agendamentos = repository.findAllByDataAgendamentoBetween(hoje, dataFinalConsulta)
                .stream()
                .filter(agendamentoVeterinario -> !StatusAgendamento.CONCLUIDO.equals(agendamentoVeterinario.getStatusAgendamento()))
                .collect(Collectors.toList());

        for (int i = 0; i < dias; i++) {
            int finalI = i;
            Arrays.stream(PeriodoDia.values()).iterator().forEachRemaining(periodoDia -> {
                verificarDisponibilidadeNoPeriodo(periodoDia, agendamentos, disponibilidadesDtos, dataPesquisa.plusDays(finalI), exibirDiasIndisponiveis);
            });
        }

        return disponibilidadesDtos;
    }

    private void verificarDisponibilidadeNoPeriodo(
            PeriodoDia periodoDia,
            List<AgendamentoVeterinario> agendamentos,
            List<DiasEDisponibilidadesDto> disponibilidadesDtos,
            LocalDate dataPesquisa,
            Boolean exibirDiasIndisponiveis) {
        boolean naoEncontrou = agendamentos.stream()
                .filter(agendamentoVeterinario -> periodoDia.equals(agendamentoVeterinario.getPeriodoDia()))
                .filter(agendamentoVeterinario -> dataPesquisa.isEqual(agendamentoVeterinario.getDataAgendamento()))
                .findAny()
                .isEmpty();

        if (naoEncontrou) {
            disponibilidadesDtos.add(DiasEDisponibilidadesDto.builder()
                    .diaNumero(dataPesquisa.getDayOfMonth())
                    .mesNumero(dataPesquisa.getMonthValue())
                    .periodoDia(periodoDia)
                    .disponivel(true)
                    .build());

            return;
        }

        if (exibirDiasIndisponiveis) {
            disponibilidadesDtos.add(DiasEDisponibilidadesDto.builder()
                    .diaNumero(dataPesquisa.getDayOfMonth())
                    .mesNumero(dataPesquisa.getMonthValue())
                    .periodoDia(periodoDia)
                    .disponivel(false)
                    .build());
        }
    }
}

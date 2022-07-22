package com.binno.dominio.module.veterinaria.repository;

import com.binno.dominio.module.veterinaria.model.AgendamentoVeterinario;
import com.binno.dominio.shared.BasicRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AgendamentoVeterinarioRepository extends BasicRepository<AgendamentoVeterinario, Integer> {

    List<AgendamentoVeterinario> findAllByDataAgendamento(LocalDate dataAgendamento);

    List<AgendamentoVeterinario> findAllByDataAgendamentoBetween(LocalDate inicio, LocalDate fim);

    @Query("select a from AgendamentoVeterinario a where a.statusAgendamento = '0'")
    List<AgendamentoVeterinario> findAllPendentes();
}

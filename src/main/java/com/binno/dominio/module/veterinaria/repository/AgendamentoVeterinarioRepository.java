package com.binno.dominio.module.veterinaria.repository;

import com.binno.dominio.module.veterinaria.model.AgendamentoVeterinario;
import com.binno.dominio.shared.BasicRepository;

import java.time.LocalDate;
import java.util.List;

public interface AgendamentoVeterinarioRepository extends BasicRepository<AgendamentoVeterinario, Integer> {

    List<AgendamentoVeterinario> findAllByDataAgendamentoAndTenantId(LocalDate dataAgendamento, Integer tenantId);

    List<AgendamentoVeterinario> findAllByDataAgendamento(LocalDate dataAgendamento);

    List<AgendamentoVeterinario> findAllByDataAgendamentoBetweenAndAndTenantId(LocalDate inicio, LocalDate fim, Integer tenantId);
}

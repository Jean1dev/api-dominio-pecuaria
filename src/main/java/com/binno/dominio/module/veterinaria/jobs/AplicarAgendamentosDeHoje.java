package com.binno.dominio.module.veterinaria.jobs;

import com.binno.dominio.module.prontuario.service.VincularVisitaVeterinariaNoProntuario;
import com.binno.dominio.module.veterinaria.model.StatusAgendamento;
import com.binno.dominio.module.veterinaria.repository.AgendamentoVeterinarioRepository;
import com.binno.dominio.shared.ApplicationJobExecutor;
import com.binno.dominio.shared.ApplicationJobs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@Transactional
public class AplicarAgendamentosDeHoje extends ApplicationJobExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AplicarAgendamentosDeHoje.class);

    private final ApplicationJobs applicationJobs;
    private final AgendamentoVeterinarioRepository repository;
    private final VincularVisitaVeterinariaNoProntuario vincularVisitaVeterinariaNoProntuario;

    public AplicarAgendamentosDeHoje(ApplicationJobs applicationJobs, AgendamentoVeterinarioRepository repository, VincularVisitaVeterinariaNoProntuario vincularVisitaVeterinariaNoProntuario) {
        super(applicationJobs);
        this.applicationJobs = applicationJobs;
        this.repository = repository;
        this.vincularVisitaVeterinariaNoProntuario = vincularVisitaVeterinariaNoProntuario;
    }

    @Override
    public void run() {
        repository.findAllByDataAgendamento(LocalDate.now())
                .stream()
                .filter(agendamentoVeterinario -> StatusAgendamento.APROVADO.equals(agendamentoVeterinario.getStatusAgendamento()))
                .forEach(agendamentoVeterinario -> {
                    LOGGER.info("processando agendamento " + agendamentoVeterinario.getId());
                    agendamentoVeterinario.setStatusAgendamento(StatusAgendamento.CONCLUIDO);
                    repository.save(agendamentoVeterinario);
                    vincularVisitaVeterinariaNoProntuario.executar(agendamentoVeterinario);
                });
    }
}

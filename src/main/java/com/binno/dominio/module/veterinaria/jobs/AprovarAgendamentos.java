package com.binno.dominio.module.veterinaria.jobs;

import com.binno.dominio.module.notificacao.service.RegistrarNotificacao;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.veterinaria.model.AgendamentoVeterinario;
import com.binno.dominio.module.veterinaria.model.StatusAgendamento;
import com.binno.dominio.module.veterinaria.repository.AgendamentoVeterinarioRepository;
import com.binno.dominio.shared.ApplicationJobExecutor;
import com.binno.dominio.shared.ApplicationJobs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class AprovarAgendamentos extends ApplicationJobExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AprovarAgendamentos.class);

    private final AgendamentoVeterinarioRepository repository;
    private final RegistrarNotificacao registrarNotificacao;

    public AprovarAgendamentos(ApplicationJobs applicationJobs, AgendamentoVeterinarioRepository repository, RegistrarNotificacao registrarNotificacao) {
        super(applicationJobs);
        this.repository = repository;
        this.registrarNotificacao = registrarNotificacao;
    }

    @Override
    public void run() {
        LOGGER.info("executando AprovarAgendamentos");
        List<AgendamentoVeterinario> collect = repository.findAllPendentes()
                .stream()
                .peek(agendamentoVeterinario -> agendamentoVeterinario.setStatusAgendamento(StatusAgendamento.APROVADO))
                .collect(Collectors.toList());

        LOGGER.info("Quantidade de registros a ser modificada " + collect.size());
        repository.saveAll(collect);
        enviarNotificacaoes(collect);
    }

    private void enviarNotificacaoes(List<AgendamentoVeterinario> collect) {
        collect.stream()
                .map(AgendamentoVeterinario::getTenant)
                .map(Tenant::getId)
                .distinct()
                .forEach(tenantId -> registrarNotificacao.executar("Seus agendamentos foram aprovados", tenantId));
    }
}

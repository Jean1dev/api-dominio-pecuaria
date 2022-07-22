package com.binno.dominio.module.veterinaria.service;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.medicamento.model.Medicamento;
import com.binno.dominio.module.medicamento.repository.MedicamentoRepository;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.veterinaria.api.dto.CriarAgendamentoDto;
import com.binno.dominio.module.veterinaria.model.AgendamentoVeterinario;
import com.binno.dominio.module.veterinaria.model.StatusAgendamento;
import com.binno.dominio.module.veterinaria.model.Veterinario;
import com.binno.dominio.module.veterinaria.repository.AgendamentoVeterinarioRepository;
import com.binno.dominio.module.veterinaria.repository.VeterinarioRepository;
import com.binno.dominio.provider.mail.MailProvider;
import com.binno.dominio.provider.mail.SendEmailPayload;
import com.binno.dominio.shared.RegraNegocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SolicitarAgendamentoVeterinario implements RegraNegocioService<AgendamentoVeterinario, CriarAgendamentoDto> {

    private final AuthenticationHolder holder;
    private final AgendamentoVeterinarioRepository agendamentoVeterinarioRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final MailProvider mailProvider;
    private final MedicamentoRepository medicamentoRepository;

    @Override
    public AgendamentoVeterinario executar(CriarAgendamentoDto dto) {
        verificarSePodeContinuar(dto);

        Veterinario veterinario = null;
        Set<Medicamento> medicamentos = new HashSet<>();
        if (Objects.nonNull(dto.getVeterinarioId())) {
            veterinario = veterinarioRepository.findById(dto.getVeterinarioId()).orElse(veterinarioPadrao());
        } else {
            veterinario = veterinarioPadrao();
        }

        if (Objects.nonNull(dto.getMedicamentosId()) && !dto.getMedicamentosId().isEmpty()) {
            medicamentoRepository.findAllById(dto.getMedicamentosId()).forEach(medicamentos::add);
        }

        AgendamentoVeterinario agendamentoVeterinario = agendamentoVeterinarioRepository.save(AgendamentoVeterinario.builder()
                .dataSolicitacao(LocalDate.now())
                .dataAgendamento(dto.getDataAgendamento())
                .veterinario(veterinario)
                .medicamentos(medicamentos)
                .statusAgendamento(StatusAgendamento.PENDENTE)
                .periodoDia(dto.getPeriodoDia())
                .tenant(Tenant.of(holder.getTenantId()))
                .build());

        enviarEmailParaVeterinario(veterinario, agendamentoVeterinario);
        return agendamentoVeterinario;
    }

    private Veterinario veterinarioPadrao() {
        return veterinarioRepository.findById(1).orElseGet(() ->
                veterinarioRepository.save(Veterinario.builder()
                        .nome("default")
                        .email("veterinario_padrao@binno.com")
                        .build())
        );
    }

    private void enviarEmailParaVeterinario(Veterinario veterinario, AgendamentoVeterinario agendamentoVeterinario) {
        if (Objects.isNull(veterinario))
            return;

        mailProvider.send(SendEmailPayload.builder()
                .from(mailProvider.getFrom())
                .subject("Novo agendamento")
                .text("Foi feito um novo agendamento para voce no dia " + agendamentoVeterinario.getDataAgendamento())
                .to(veterinario.getEmail())
                .build());
    }

    private void verificarSePodeContinuar(CriarAgendamentoDto dto) {
        if (LocalDate.now().isAfter(dto.getDataAgendamento())) {
            throw new ValidationException("Não é possivel agendar para uma data menor que a data de hoje");
        }

        agendamentoVeterinarioRepository.findAllByDataAgendamento(dto.getDataAgendamento()).forEach(agendamentoVeterinario -> {
            if (agendamentoVeterinario.getPeriodoDia().equals(dto.getPeriodoDia())) {
                throw new ValidationException("Ja tem um agendamento para esse periodo");
            }
        });
    }
}

package com.binno.dominio.module.vacinacao.service;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.medicamento.model.Medicamento;
import com.binno.dominio.module.medicamento.repository.MedicamentoRepository;
import com.binno.dominio.module.notificacao.service.RegistrarNotificacao;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.vacinacao.api.dto.ProcessarVacinacaoDto;
import com.binno.dominio.module.vacinacao.model.ProcessoVacinacao;
import com.binno.dominio.module.vacinacao.repository.ProcessoVacinacaoRepository;
import com.binno.dominio.shared.RegraNegocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableAsync
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProcessarVacinacao implements RegraNegocioService<ProcessoVacinacao, ProcessarVacinacaoDto> {

    private final MedicamentoRepository medicamentoRepository;

    private final AnimalRepository animalRepository;

    private final RegistrarNotificacao notificacao;

    private final ProcessoVacinacaoRepository repository;

    private final AuthenticationHolder holder;

    @Async
    @Override
    public ProcessoVacinacao executar(ProcessarVacinacaoDto dto) {
        Medicamento medicamento = medicamentoRepository.findById(dto.getMedicamentoId()).orElseThrow();

        if (!verificarValidadeDoMedicamente(medicamento))
            return null;

        List<String> animaisVacinados = new ArrayList<>();
        /*
        * nota para o futuro, aqui enquanto tiver uns 1000 registros ta dboa
        * mas futuramente o ideal é fazer por demanada pra nao estoura a memoria
        * */
        animalRepository.findAllById(dto.getAnimaisId()).iterator().forEachRemaining(animal -> {
            animaisVacinados.add(animal.getId().toString());
        });

        ProcessoVacinacao processoVacinacao = repository.save(ProcessoVacinacao.builder()
                .dataProcesso(LocalDate.now())
                .animaisIdSeparadoPorVirgula(String.join(",", animaisVacinados))
                .totalAnimaisVacinados(animaisVacinados.size())
                .processoRevertido(false)
                .tenant(Tenant.of(holder.getTenantId()))
                .medicamento(medicamento)
                .build());

        notificacao.executar("Processo de vacinação finalizado " + LocalDateTime.now());
        return processoVacinacao;
    }

    private boolean verificarValidadeDoMedicamente(Medicamento medicamento) {
        if (medicamento.getDataValidade().isBefore(LocalDate.now())) {
            notificacao.executar("Processo de vacinação falhou por que o Medicamento esta com data de validade expirada");
            return false;
        }

        return true;
    }
}

package com.binno.dominio.module.vacinacao.service;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.medicamento.model.Medicamento;
import com.binno.dominio.module.medicamento.repository.MedicamentoRepository;
import com.binno.dominio.module.notificacao.service.RegistrarNotificacao;
import com.binno.dominio.module.prontuario.api.dto.VincularProcessoVacinacaoDto;
import com.binno.dominio.module.prontuario.service.VincularProcessoVacinacaoNoProntuario;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.vacinacao.api.dto.ProcessarVacinacaoDto;
import com.binno.dominio.module.vacinacao.model.ProcessoVacinacao;
import com.binno.dominio.module.vacinacao.repository.ProcessoVacinacaoRepository;
import com.binno.dominio.shared.RegraNegocioService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableAsync
@Transactional
@RequiredArgsConstructor
public class ProcessarVacinacao implements RegraNegocioService<ProcessoVacinacao, ProcessarVacinacaoDto> {

    private static final Logger logger = LoggerFactory.getLogger(ProcessarVacinacao.class);

    private final MedicamentoRepository medicamentoRepository;

    private final AnimalRepository animalRepository;

    private final RegistrarNotificacao notificacao;

    private final ProcessoVacinacaoRepository repository;

    private final AuthenticationHolder holder;

    private final VincularProcessoVacinacaoNoProntuario vincularProcessoVacinacaoNoProntuario;

    @Async
    @Override
    public ProcessoVacinacao executar(ProcessarVacinacaoDto dto) {
        Medicamento medicamento = medicamentoRepository.findById(dto.getMedicamentoId()).orElseThrow();

        if (!applyValidations(medicamento, dto))
            return null;

        List<String> animaisVacinados = dto.getAnimaisId().stream().map(Object::toString).collect(Collectors.toList());
        logger.info("iniciando processo de vacinção :: total animais " + animaisVacinados.size());
        ProcessoVacinacao processoVacinacao = repository.save(ProcessoVacinacao.builder()
                .dataProcesso(LocalDate.now())
                .animaisIdSeparadoPorVirgula(String.join(",", animaisVacinados))
                .totalAnimaisVacinados(animaisVacinados.size())
                .processoRevertido(false)
                .tenant(Tenant.of(holder.getTenantId()))
                .medicamento(medicamento)
                .build());
        /*
         * nota para o futuro, aqui enquanto tiver uns 1000 registros ta dboa
         * mas futuramente o ideal é fazer por demanada pra nao estoura a memoria
         * */
        animalRepository.findAllById(dto.getAnimaisId()).iterator().forEachRemaining(animal -> {
            vincularProcessoVacinacaoNoProntuario.executar(VincularProcessoVacinacaoDto.builder()
                    .processoVacinacaoId(processoVacinacao.getId())
                    .animal(animal)
                    .build());
        });

        notificacao.executar("Processo de vacinação finalizado " + LocalDateTime.now());
        return processoVacinacao;
    }

    private boolean applyValidations(Medicamento medicamento, ProcessarVacinacaoDto dto) {
        if (!verificarValidadeDoMedicamente(medicamento))
            return false;

        if (!verificarSeTemAnimaisNaLista(dto.getAnimaisId()))
            return false;

        return true;
    }

    private boolean verificarSeTemAnimaisNaLista(List<Integer> animaisId) {
        if (animaisId.isEmpty()) {
            notificacao.executar("Processo de vacinação falhou por que não foi selecionado nenhum animal");
            return false;
        }

        return true;
    }

    private boolean verificarValidadeDoMedicamente(Medicamento medicamento) {
        if (medicamento.getDataValidade().isBefore(LocalDate.now())) {
            notificacao.executar("Processo de vacinação falhou por que o Medicamento esta com data de validade expirada");
            return false;
        }

        return true;
    }
}

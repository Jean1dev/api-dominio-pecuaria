package com.binno.dominio.module.fazenda.service;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.fazenda.model.Fazenda;
import com.binno.dominio.module.fazenda.repository.FazendaRepository;
import com.binno.dominio.module.notificacao.service.RegistrarNotificacao;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@EnableAsync
@RequiredArgsConstructor
public class RebalanceamentoAnimaisNasFazendasService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RebalanceamentoAnimaisNasFazendasService.class);

    private final FazendaRepository fazendaRepository;
    private final AnimalRepository animalRepository;
    private final RegistrarNotificacao registrarNotificacao;
    private final AuthenticationHolder holder;

    /*
     * @Docs
     * objetivo desse metodo é deixar todas as fazendas com animais que atendam a sua capacidade maxima
     * */
    @Async
    public void balancear() {
        List<Fazenda> fazendas = fazendaRepository.findAllByTenantId(holder.getTenantId());
        List<Fazenda> fazendasComCapacidadesEstourada = fazendas.stream()
                .filter(RebalanceamentoAnimaisNasFazendasService::fazendaValida)
                .filter(fazenda -> fazenda.getAnimais().size() > fazenda.getCapMaximaGado())
                .collect(Collectors.toList());

        if (fazendasComCapacidadesEstourada.isEmpty()) {
            registrarNotificacao.executar("Não tem necessidade de fazer balanceamento nas fazendas");
            return;
        }

        List<Fazenda> fazendasDisponiveis = fazendas.stream()
                .filter(RebalanceamentoAnimaisNasFazendasService::fazendaValida)
                .filter(fazenda -> fazenda.getCapMaximaGado() > fazenda.getAnimais().size())
                .collect(Collectors.toList());

        if (fazendasDisponiveis.isEmpty()) {
            registrarNotificacao.executar("Não existem fazendas disponveis para o balanceamento");
            return;
        }

        fazendasComCapacidadesEstourada.forEach(fazenda -> {
            int quantidadeAnimaisExcedentes = fazenda.getAnimais().size() - fazenda.getCapMaximaGado();
            levarAnimaisParaOutraFazenda(quantidadeAnimaisExcedentes, fazenda, fazendasDisponiveis);
        });

        registrarNotificacao.executar("Balanceamento finalizado");
    }

    private static boolean fazendaValida(Fazenda fazenda) {
        return !Objects.isNull(fazenda.getCapMaximaGado());
    }

    private void levarAnimaisParaOutraFazenda(int quantidadeAnimaisExcedentes, Fazenda fazenda, List<Fazenda> fazendasDisponiveis) {
        fazendasDisponiveis.stream()
                .filter(f1 -> !f1.equals(fazenda))
                .filter(f1 -> !f1.getBalanceada())
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .ifPresent(fazendaASerBalanceada -> {
                    int quantidadePossivelInserir = fazendaASerBalanceada.getCapMaximaGado() - fazendaASerBalanceada.getAnimais().size();
                    List<Animal> animais = new ArrayList<>();
                    for (int i = 0; i < quantidadeAnimaisExcedentes; i++) {
                        Animal animal = fazenda.getAnimais().get(i);

                        if (animais.size() < quantidadePossivelInserir) {
                            LOGGER.info(animal.getApelido() + " saiu da fazenda " + animal.getFazenda().getNome() + " para " + fazendaASerBalanceada.getNome());
                            animal.setFazenda(fazendaASerBalanceada);
                            animais.add(animal);
                        }
                    }

                    fazendaASerBalanceada.setBalanceada(true);
                    LOGGER.info(fazendaASerBalanceada.getNome() + " balanceado");

                    if (!animais.isEmpty()) {
                        animalRepository.saveAll(animais);
                    }

                });
    }
}

package com.binno.dominio.module.animal.service;

import com.binno.dominio.module.animal.api.dto.TransferirAnimalEntreFazendaDto;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.fazenda.model.Fazenda;
import com.binno.dominio.module.fazenda.repository.FazendaRepository;
import com.binno.dominio.module.notificacao.service.RegistrarNotificacao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@EnableAsync
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransferirAnimalEntreFazendasService {

    private final FazendaRepository fazendaRepository;

    private final AnimalRepository animalRepository;

    private final RegistrarNotificacao notificacao;

    @Async
    public void executar(TransferirAnimalEntreFazendaDto dto) {
        Fazenda fazendaOrigem = fazendaRepository.findById(dto.getFazendaOrigemId()).orElseThrow();
        Fazenda fazendaDestino = fazendaRepository.findById(dto.getFazendaDestinoId()).orElseThrow();

        if (dto.getTodosOsAnimais()) {
            transferirTodosOsAnimais(fazendaOrigem, fazendaDestino);
            return;
        }

        AtomicInteger total = new AtomicInteger();
        animalRepository.findAllById(dto.getAnimaisSelecionadosId()).iterator().forEachRemaining(animal -> {
            total.getAndIncrement();
            animal.setFazenda(fazendaDestino);
            animalRepository.save(animal);
        });

        notificacao.executar(total + " animais transferidos para a fazenda: " + fazendaDestino.getNome());
    }

    private void transferirTodosOsAnimais(Fazenda fazendaOrigem, Fazenda fazendaDestino) {
        List<Animal> animalList = animalRepository.findAllByFazendaId(fazendaOrigem.getId());
        animalList.forEach(animal -> {
            animal.setFazenda(fazendaDestino);
            animalRepository.save(animal);
        });

        notificacao.executar(animalList.size() + " animais transferidos para a fazenda: " + fazendaDestino.getNome());
    }
}

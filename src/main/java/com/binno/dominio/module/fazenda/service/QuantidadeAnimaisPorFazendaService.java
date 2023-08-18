package com.binno.dominio.module.fazenda.service;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.fazenda.api.dto.QuantidadeAnimalPorFazendaDto;
import com.binno.dominio.module.fazenda.repository.FazendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuantidadeAnimaisPorFazendaService {

    private final AuthenticationHolder holder;
    private final AnimalRepository animalRepository;
    private final FazendaRepository fazendaRepository;

    public List<QuantidadeAnimalPorFazendaDto> gerarConsulta() {
        return fazendaRepository.findAllByTenantId(holder.getTenantId())
                .stream()
                .map(fazenda -> {
                    long totalNessaFazenda = animalRepository.countAllByTenantIdAndFazendaId(holder.getTenantId(), fazenda.getId());
                    return QuantidadeAnimalPorFazendaDto.builder()
                            .idFazenda(fazenda.getId())
                            .nomeFazenda(fazenda.getNome())
                            .capacidadeMaximaFazenda(fazenda.getCapMaximaGado())
                            .totalAnimais(totalNessaFazenda)
                            .build();
                }).collect(Collectors.toList());
    }
}

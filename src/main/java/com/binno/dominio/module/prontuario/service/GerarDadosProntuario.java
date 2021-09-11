package com.binno.dominio.module.prontuario.service;

import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.prontuario.api.dto.DadosProntuarioDto;
import com.binno.dominio.module.prontuario.model.Prontuario;
import com.binno.dominio.module.prontuario.repository.ProntuarioRepository;
import com.binno.dominio.module.vacinacao.model.ProcessoVacinacao;
import com.binno.dominio.module.vacinacao.repository.ProcessoVacinacaoRepository;
import com.binno.dominio.shared.RegraNegocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GerarDadosProntuario implements RegraNegocioService<DadosProntuarioDto, Integer> {

    private final ProntuarioRepository repository;

    private final ProcessoVacinacaoRepository processoVacinacaoRepository;

    @Override
    public DadosProntuarioDto executar(Integer animalId) {
        Prontuario prontuario = repository.findByAnimalId(animalId).orElseThrow();
        Animal animal = prontuario.getAnimal();

        List<ProcessoVacinacao> vacinacaoList = Arrays.stream(prontuario.getVacinacaoSeparadoPorVirgula()
                        .split(","))
                .collect(Collectors.toList())
                .stream()
                .filter(id -> !id.isEmpty())
                .map(String::trim)
                .map(Integer::valueOf)
                .map(processoVacinacaoRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return DadosProntuarioDto.builder()
                .animal(animal)
                .prontuario(prontuario)
                .processoVacinacaoList(vacinacaoList)
                .build();
    }
}

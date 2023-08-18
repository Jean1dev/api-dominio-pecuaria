package com.binno.dominio.module.prontuario.service;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.prontuario.api.dto.DadosProntuarioDto;
import com.binno.dominio.module.prontuario.model.Prontuario;
import com.binno.dominio.module.prontuario.repository.ProntuarioRepository;
import com.binno.dominio.module.vacinacao.model.ProcessoVacinacao;
import com.binno.dominio.module.vacinacao.repository.ProcessoVacinacaoRepository;
import com.binno.dominio.shared.RegraNegocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GerarDadosProntuario implements RegraNegocioService<DadosProntuarioDto, Integer> {

    private final ProntuarioRepository repository;

    private final ProcessoVacinacaoRepository processoVacinacaoRepository;

    private final AuthenticationHolder holder;

    @Override
    public DadosProntuarioDto executar(Integer animalId) {
        Prontuario prontuario = repository.findByAnimalId(animalId).orElseThrow();
        return getProntuarioDto(prontuario);
    }

    private DadosProntuarioDto getProntuarioDto(Prontuario prontuario) {
        Animal animal = prontuario.getAnimal();

        List<ProcessoVacinacao> vacinacaoList = extrairListaVacinacaoByProntuario(prontuario);

        return DadosProntuarioDto.builder()
                .animal(animal)
                .prontuario(prontuario)
                .processoVacinacaoList(vacinacaoList)
                .build();
    }

    private List<ProcessoVacinacao> extrairListaVacinacaoByProntuario(Prontuario prontuario) {
        return Arrays.stream(prontuario.getVacinacaoSeparadoPorVirgula()
                        .split(","))
                .toList()
                .stream()
                .filter(id -> !id.isEmpty())
                .map(String::trim)
                .map(Integer::valueOf)
                .map(processoVacinacaoRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<DadosProntuarioDto> recuperarProntuariosDosMeusAnimais() {
        return repository.findAllByTenantId(holder.getTenantId())
                .stream()
                .map(this::getProntuarioDto)
                .collect(Collectors.toList());
    }
}

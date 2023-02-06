package com.binno.dominio.module.prontuario.service;

import com.binno.dominio.module.prontuario.api.dto.DadosProntuarioDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableAsync
public class AtualizarProntuarios {

    private final GerarDadosProntuario gerarDadosProntuario;

    @Async
    public void atualizarProntuarios() {
        LocalDate hoje = LocalDate.now();
        List<DadosProntuarioDto> prontuarioDtos = gerarDadosProntuario.recuperarProntuariosDosMeusAnimais()
                .stream().filter(dadosProntuarioDto -> {
                    boolean maiorQue30Dias = false;
                    LocalDate dataCriacao = dadosProntuarioDto.getProntuario().getDataCriacao();
                    LocalDate ultimaAtualizacao = dadosProntuarioDto.getProntuario().getDataUltimaAtualizacao();

                    LocalDate dataAdd30dias;
                    if (Objects.nonNull(ultimaAtualizacao)) {
                        dataAdd30dias = ultimaAtualizacao.plusDays(30);

                    } else {
                        dataAdd30dias = dataCriacao.plusDays(30);
                    }

                    if (hoje.isAfter(dataAdd30dias))
                        maiorQue30Dias = true;

                    return maiorQue30Dias;
                }).toList();

        if (prontuarioDtos.isEmpty())
            return;

        evidenciarCasos(prontuarioDtos);
    }

    private void evidenciarCasos(List<DadosProntuarioDto> prontuarioDtos) {
        prontuarioDtos.forEach(dadosProntuarioDto -> {
            log.info("evidenciando prontuario "  + dadosProntuarioDto.getAnimal().getId());
        });
    }
}

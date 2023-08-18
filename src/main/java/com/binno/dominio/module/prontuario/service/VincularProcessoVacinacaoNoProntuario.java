package com.binno.dominio.module.prontuario.service;

import com.binno.dominio.module.prontuario.api.dto.VincularProcessoVacinacaoDto;
import com.binno.dominio.module.prontuario.model.Prontuario;
import com.binno.dominio.module.prontuario.repository.ProntuarioRepository;
import com.binno.dominio.shared.RegraNegocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class VincularProcessoVacinacaoNoProntuario implements RegraNegocioService<Prontuario, VincularProcessoVacinacaoDto> {

    private final ProntuarioRepository repository;

    @Override
    public Prontuario executar(VincularProcessoVacinacaoDto dto) {
        Prontuario prontuario = repository.findByAnimalId(dto.getAnimal().getId()).orElseGet(() -> repository.save(Prontuario.builder()
                .tenant(dto.getAnimal().getTenant())
                .animal(dto.getAnimal())
                .dataCriacao(LocalDate.now())
                .vacinacaoSeparadoPorVirgula("")
                .dataUltimaAtualizacao(LocalDate.now())
                .build()));

        prontuario.setDataUltimaAtualizacao(LocalDate.now());
        prontuario.setVacinacaoSeparadoPorVirgula(prontuario.getVacinacaoSeparadoPorVirgula() + ", " + dto.getProcessoVacinacaoId());
        return repository.save(prontuario);
    }
}

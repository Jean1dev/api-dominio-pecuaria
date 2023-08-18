package com.binno.dominio.module.prontuario.service;

import com.binno.dominio.module.prontuario.model.Prontuario;
import com.binno.dominio.module.prontuario.repository.ProntuarioRepository;
import com.binno.dominio.module.veterinaria.model.AgendamentoVeterinario;
import com.binno.dominio.shared.RegraNegocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class VincularVisitaVeterinariaNoProntuario implements RegraNegocioService<Prontuario, AgendamentoVeterinario> {

    private final ProntuarioRepository repository;

    @Override
    public Prontuario executar(AgendamentoVeterinario dto) {
        List<Prontuario> prontuarios = new ArrayList<>();
        repository.findAllByTenantId(dto.getTenant().getId()).forEach(prontuario -> {
            if (Objects.isNull(prontuario.getParecerVeterinarioSeparadoPorVirgula()))
                prontuario.setParecerVeterinarioSeparadoPorVirgula("");

            prontuario.setParecerVeterinarioSeparadoPorVirgula(prontuario.getParecerVeterinarioSeparadoPorVirgula() + ", " + dto.getId());
            prontuarios.add(prontuario);
        });

        repository.saveAll(prontuarios);
        return null;
    }
}

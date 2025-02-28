package com.binno.dominio.module.animal.service;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimalConsultasService {

    private final AnimalRepository repository;

    private final AuthenticationHolder holder;

    public long totalPorSexo(Boolean isFemea) {
        return repository.countAnimalByIsFemeaAndTenantId(isFemea, holder.getTenantId());
    }

    public long total() {
        return repository.countAllByTenantId(holder.getTenantId());
    }

    public long getUltimoNumeroDoAnimalCadastrado() {
        return repository.findMaxNumero(holder.getTenantId()).orElseGet(() -> 1L);
    }
}

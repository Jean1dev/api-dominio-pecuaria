package com.binno.dominio.module.animal.service;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimalConsultasService {

    @Autowired
    private AnimalRepository repository;

    @Autowired
    private AuthenticationHolder holder;

    public long totalPorSexo(Boolean isFemea) {
        return repository.countAnimalByIsFemeaAndTenantId(isFemea, holder.getTenantId());
    }

    public long total() {
        return repository.countAllByTenantId(holder.getTenantId());
    }
}

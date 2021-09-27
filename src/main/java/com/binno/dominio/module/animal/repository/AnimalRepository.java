package com.binno.dominio.module.animal.repository;

import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.shared.BasicRepository;

import java.util.List;

public interface AnimalRepository extends BasicRepository<Animal, Integer> {

    long countAnimalByIsFemeaAndTenantId(Boolean isFemea, Integer tenant);

    long countAllByTenantId(Integer tenant);

    List<Animal> findAllByFazendaId(Integer fazendaId);
}

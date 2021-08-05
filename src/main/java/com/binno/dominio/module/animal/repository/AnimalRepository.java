package com.binno.dominio.module.animal.repository;

import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.shared.BasicRepository;

public interface AnimalRepository extends BasicRepository<Animal, Integer> {

    long countAnimalByIsFemeaAndTenantId(Boolean isFemea, Integer tenant);

    long countAllByTenantId(Integer tenant);
}

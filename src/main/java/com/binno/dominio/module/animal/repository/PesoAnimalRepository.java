package com.binno.dominio.module.animal.repository;

import com.binno.dominio.module.animal.model.PesoAnimal;
import com.binno.dominio.shared.BasicRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PesoAnimalRepository extends BasicRepository<PesoAnimal, Integer> {
}

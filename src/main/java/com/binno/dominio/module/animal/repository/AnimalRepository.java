package com.binno.dominio.module.animal.repository;

import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.shared.BasicRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends BasicRepository<Animal, Integer> {
}

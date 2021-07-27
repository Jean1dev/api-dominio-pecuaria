package com.binno.dominio.module.animal.repository;

import com.binno.dominio.module.animal.model.PesoAnimal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PesoAnimalRepository extends JpaRepository<PesoAnimal, Integer> {
}

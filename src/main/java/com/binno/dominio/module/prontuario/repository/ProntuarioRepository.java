package com.binno.dominio.module.prontuario.repository;

import com.binno.dominio.module.prontuario.model.Prontuario;
import com.binno.dominio.shared.BasicRepository;

import java.util.Optional;

public interface ProntuarioRepository extends BasicRepository<Prontuario, Integer> {

    Optional<Prontuario> findByAnimalId(Integer animalId);
}

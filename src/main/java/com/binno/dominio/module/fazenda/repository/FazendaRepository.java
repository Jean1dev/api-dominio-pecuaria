package com.binno.dominio.module.fazenda.repository;

import com.binno.dominio.module.fazenda.model.Fazenda;
import com.binno.dominio.shared.BasicRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FazendaRepository extends BasicRepository<Fazenda, Integer> {
}

package com.binno.dominio.module.medicamento.repository;

import com.binno.dominio.module.medicamento.model.Medicamento;
import com.binno.dominio.shared.BasicRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentoRepository extends BasicRepository<Medicamento, Integer> {
}

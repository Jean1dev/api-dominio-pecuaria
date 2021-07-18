package com.binno.dominio.module.funcionario.repository;

import com.binno.dominio.module.funcionario.model.Funcionario;
import com.binno.dominio.shared.BasicRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioRepository extends BasicRepository<Funcionario, Integer> {
}

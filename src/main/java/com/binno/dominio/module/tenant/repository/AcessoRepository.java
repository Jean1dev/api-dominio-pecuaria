package com.binno.dominio.module.tenant.repository;

import com.binno.dominio.module.tenant.model.Acessos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcessoRepository extends JpaRepository<Acessos, Integer> {
}

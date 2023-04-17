package com.binno.dominio.module.tenant.repository;

import com.binno.dominio.module.tenant.model.Acesso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcessoRepository extends JpaRepository<Acesso, Integer> {
}

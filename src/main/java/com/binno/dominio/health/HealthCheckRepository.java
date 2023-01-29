package com.binno.dominio.health;

import com.binno.dominio.module.usuarioacesso.model.UsuarioAcesso;
import com.binno.dominio.shared.BasicRepository;
import org.springframework.data.jpa.repository.Query;


public interface HealthCheckRepository extends BasicRepository<UsuarioAcesso, Integer> {
    @Query(value = "select 1", nativeQuery = true)
    Integer connectionBD();
}

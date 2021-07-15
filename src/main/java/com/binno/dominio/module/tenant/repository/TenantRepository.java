package com.binno.dominio.module.tenant.repository;

import com.binno.dominio.module.tenant.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Integer> {
}

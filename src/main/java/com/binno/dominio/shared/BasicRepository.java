package com.binno.dominio.shared;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BasicRepository<T, ID> extends PagingAndSortingRepository<T, ID>, JpaSpecificationExecutor<T> {

    Page<T> findAllByTenantId(Pageable pageable, Integer tenantId);

    List<T> findAllByTenantId(Integer tenantId);

    Optional<T> findOneByTenantId(Integer tenantId);

    long countAllByTenantId(Integer tenantId);
}

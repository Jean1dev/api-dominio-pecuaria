package com.binno.dominio.module.animal.repository;

import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.shared.BasicRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends BasicRepository<Animal, Integer> {

    long countAnimalByIsFemeaAndTenantId(Boolean isFemea, Integer tenant);

    long countAllByTenantId(Integer tenant);

    long countAllByTenantIdAndFazendaId(Integer integer, Integer fazendaId);

    @Query(value = "select max (numero) from Animal where tenant.id = ?1")
    Optional<Long> findMaxNumero(Integer tenantId);

    List<Animal> findAllByFazendaId(Integer fazendaId);
}

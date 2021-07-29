package com.binno.dominio.module.animal.specification;

import com.binno.dominio.module.animal.model.Animal;
import org.springframework.data.jpa.domain.Specification;

public class AnimalSpecification {

    public static Specification<Animal> numero(Integer numero) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("numero"), numero);
    }

    public static Specification<Animal> tenant(Integer id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant"), id);
    }
}

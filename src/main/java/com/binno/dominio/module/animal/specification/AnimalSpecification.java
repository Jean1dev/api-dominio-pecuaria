package com.binno.dominio.module.animal.specification;

import com.binno.dominio.module.animal.model.Animal;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class AnimalSpecification {

    public static Specification<Animal> numero(Integer numero) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("numero"), numero);
    }

    public static Specification<Animal> intervaloNumeracao(Integer inicial, Integer nfinal) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get("numero"), inicial, nfinal);
    }

    public static Specification<Animal> tenant(Integer id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant"), id);
    }

    public static Specification<Animal> where() {
        return new Specification<Animal>() {
            @Override
            public Predicate toPredicate(Root<Animal> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return null;
            }
        };
    }
}

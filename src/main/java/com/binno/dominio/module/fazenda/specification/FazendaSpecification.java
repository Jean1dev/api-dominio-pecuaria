package com.binno.dominio.module.fazenda.specification;

import com.binno.dominio.module.fazenda.model.Fazenda;
import org.springframework.data.jpa.domain.Specification;

public class FazendaSpecification {

    public static Specification<Fazenda> nome(String nome) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("nome"), "%" + nome);
    }

    public static Specification<Fazenda> tenant(Integer id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant"), id);
    }
}

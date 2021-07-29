package com.binno.dominio.module.funcionario.specification;

import com.binno.dominio.module.funcionario.model.Funcionario;
import org.springframework.data.jpa.domain.Specification;

public class FuncionarioSpecification {

    public static Specification<Funcionario> nome(String nome) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("nome"), "%" + nome);
    }

    public static Specification<Funcionario> tenant(Integer id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant"), id);
    }
}

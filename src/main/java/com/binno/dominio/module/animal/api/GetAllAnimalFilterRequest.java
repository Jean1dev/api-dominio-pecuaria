package com.binno.dominio.module.animal.api;

import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.specification.AnimalSpecification;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

import static com.binno.dominio.module.animal.specification.AnimalSpecification.tenant;

@Data
public class GetAllAnimalFilterRequest {

    private Integer numero;
    private Integer numeroInicial;
    private Integer numeroFinal;
    private String orderByNumero;
    private Boolean descarteFuturo = false;

    public Specification<Animal> buildSpecification(Integer tenantId) {
        Specification<Animal> specs = AnimalSpecification.where();

        specs = specs.and(tenant(tenantId));

        if (descarteFuturo)
            specs = specs.and(AnimalSpecification.descarteFuturo());

        if (numero != null)
            specs = specs.and(AnimalSpecification.numero(numero));

        if (numeroInicial != null && numeroFinal != null)
            specs = specs.and(AnimalSpecification.intervaloNumeracao(numeroInicial, numeroFinal));

        return specs;
    }

    public Pageable buildSortAndPageable(int page, int size) {
        if (Objects.isNull(orderByNumero)) {
            return PageRequest.of(page, size);
        }

        if (orderByNumero.equalsIgnoreCase("ASC")) {
            return PageRequest.of(page, size, Sort.by(Sort.Order.asc("numero")));
        } else {
            return PageRequest.of(page, size, Sort.by(Sort.Order.desc("numero")));
        }

    }
}

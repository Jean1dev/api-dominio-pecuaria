package com.binno.dominio.module.animal.api;

import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.specification.AnimalSpecification;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import static com.binno.dominio.module.animal.specification.AnimalSpecification.tenant;

@Data
public class GetAllAnimalFilterRequest {

    private Integer numero;
    private Integer numeroInicial;
    private Integer numeroFinal;

    public Specification<Animal> buildSpecification(Integer tenantId) {
        Specification<Animal> specs = AnimalSpecification.where();

        specs = specs.and(tenant(tenantId));

        if (numero != null)
            specs = specs.and(AnimalSpecification.numero(numero));

        if (numeroInicial != null && numeroFinal != null)
            specs = specs.and(AnimalSpecification.intervaloNumeracao(numeroInicial, numeroFinal));

        return specs;
    }
}

package com.binno.dominio.module.tenant.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tenant")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private Boolean ativo = true;
}

package com.binno.dominio.module.funcionario.model;

import com.binno.dominio.module.fazenda.model.Fazenda;
import com.binno.dominio.module.tenant.model.Tenant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "{funcionario.nome.notnull}")
    private String nome;
    private String cargo;
    private String rg;
    private String cpf;
    @NotNull(message = "{fazenda.fazenda.notnull}")
    @ManyToOne(fetch = FetchType.LAZY)
    private Fazenda fazenda;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant", referencedColumnName = "id")
    private Tenant tenant;
}

package com.binno.dominio.module.fazenda.model;

import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.funcionario.model.Funcionario;
import com.binno.dominio.module.tenant.model.Tenant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Fazenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "O nome não pode ser nulo ou vazio.")
    private String nome;
    private Integer codEstab;
    private String endereco;
    private Integer metragem;
    private TipoMetragem tipoMetragem;
    private Integer capMaximaGado;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fazenda", cascade = CascadeType.ALL)
    private List<Animal> animais;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fazenda", cascade = CascadeType.ALL)
    private List<Funcionario> funcionarios;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant", referencedColumnName = "id")
    private Tenant tenant;
}

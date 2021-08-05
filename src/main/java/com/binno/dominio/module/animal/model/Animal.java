package com.binno.dominio.module.animal.model;

import com.binno.dominio.module.fazenda.model.Fazenda;
import com.binno.dominio.module.imagem.model.Imagem;
import com.binno.dominio.module.tenant.model.Tenant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "animal")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "O número não pode ser nulo ou vazio.")
    private Integer numero;
    private RacaAnimal raca;
    private String apelido;
    private LocalDate dataNascimento;
    private Integer numeroCrias;
    private EstadoAtual estadoAtual;
    private LocalDate dataUltimoParto;
    private Boolean descarteFuturo = false;
    private String justificativaDescarteFuturo;
    @NotNull(message = "É obrigatorio informar o sexo do animal")
    private Boolean isFemea = true;
    @ManyToOne
    private Fazenda fazenda;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "animal", cascade = CascadeType.ALL)
    private List<PesoAnimal> pesoAnimal;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "referenciaAnimal", cascade = CascadeType.ALL)
    private Collection<Imagem> imagens;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant", referencedColumnName = "id")
    private Tenant tenant;
}

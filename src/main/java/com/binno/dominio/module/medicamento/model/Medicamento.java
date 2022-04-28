package com.binno.dominio.module.medicamento.model;

import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.veterinaria.model.AgendamentoVeterinario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "O nome não pode ser nulo ou vazio.")
    private String nome;
    private String descricao;
    private LocalDate dataValidade;

    @ManyToMany(mappedBy = "medicamentos", fetch = FetchType.LAZY)
    private Set<AgendamentoVeterinario> agendamentos;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant", referencedColumnName = "id")
    private Tenant tenant;
}

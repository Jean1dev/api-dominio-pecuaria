package com.binno.dominio.module.veterinaria.model;

import com.binno.dominio.module.medicamento.model.Medicamento;
import com.binno.dominio.module.tenant.model.Tenant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "agendamento_veterinario")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoVeterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataSolicitacao;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataAgendamento;

    @NotNull
    private PeriodoDia periodoDia;

    private String observacoesVeterinario;

    private StatusAgendamento statusAgendamento;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "agendamentos_veterinario_medicamentos",
            joinColumns = @JoinColumn(name = "agendamento_veterinario_id"),
            inverseJoinColumns = @JoinColumn(name = "medicamento_id"))
    private Set<Medicamento> medicamentos;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinario", referencedColumnName = "id")
    private Veterinario veterinario;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant", referencedColumnName = "id")
    private Tenant tenant;
}

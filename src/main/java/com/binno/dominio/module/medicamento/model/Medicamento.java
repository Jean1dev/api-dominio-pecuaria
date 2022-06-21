package com.binno.dominio.module.medicamento.model;

import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.veterinaria.model.AgendamentoVeterinario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import static com.binno.dominio.shared.utils.BinnoUtills.ifNullReturnValue;

@Getter
@Builder
@NoArgsConstructor
@Entity
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "O nome não pode ser nulo ou vazio.")
    private String nome;
    private String descricao;
    private LocalDate dataValidade;
    @Transient
    private EstadoMedicamento estadoMedicamento;

    @ManyToMany(mappedBy = "medicamentos", fetch = FetchType.LAZY)
    private Set<AgendamentoVeterinario> agendamentos;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant", referencedColumnName = "id")
    private Tenant tenant;

    @Builder
    public Medicamento(Integer id,
                       String nome,
                       String descricao,
                       LocalDate dataValidade,
                       EstadoMedicamento estadoMedicamento,
                       Set<AgendamentoVeterinario> agendamentos,
                       Tenant tenant) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataValidade = (LocalDate) ifNullReturnValue(dataValidade, LocalDate.now());
        this.estadoMedicamento = retornaEstadoDisponivel();
        this.agendamentos = agendamentos;
        this.tenant = tenant;
    }

    @PostLoad
    public void fillTransientProperties() {
        this.estadoMedicamento = retornaEstadoDisponivel();
    }

    private EstadoMedicamento retornaEstadoDisponivel() {
        if (Objects.isNull(dataValidade) || dataValidade.isBefore(LocalDate.now()))
            return EstadoMedicamento.VENCIDO;

        return EstadoMedicamento.DISPONIVEL;
    }
}

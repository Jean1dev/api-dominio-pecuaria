package com.binno.dominio.module.vacinacao.model;

import com.binno.dominio.module.medicamento.model.Medicamento;
import com.binno.dominio.module.tenant.model.Tenant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@Table(name = "processo_vacinacao")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessoVacinacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Boolean processoRevertido = false;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataProcesso;

    @Positive
    private Integer totalAnimaisVacinados;

    @Column(name = "animais_id_lista")
    private String animaisIdSeparadoPorVirgula;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicamento", referencedColumnName = "id")
    private Medicamento medicamento;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant", referencedColumnName = "id")
    private Tenant tenant;
}

package com.binno.dominio.module.prontuario.model;

import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.tenant.model.Tenant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "prontuario")
public class Prontuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataUltimaAtualizacao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataCriacao;

    private String observacoes;

    @Column(name = "lista_processos_vacinacao")
    private String vacinacaoSeparadoPorVirgula;

    @Column(name = "lista_parecer_veterinario")
    private String parecerVeterinarioSeparadoPorVirgula;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant", referencedColumnName = "id")
    private Tenant tenant;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal", referencedColumnName = "id")
    private Animal animal;
}

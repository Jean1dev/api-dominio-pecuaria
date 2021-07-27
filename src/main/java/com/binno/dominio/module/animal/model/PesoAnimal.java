package com.binno.dominio.module.animal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "peso_animal")
public class PesoAnimal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate dataPesagem;
    private Integer peso;
    @Column(name = "idade_em_dias")
    private Integer idadeEmDias;
    @ManyToOne
    private Animal animal;
}

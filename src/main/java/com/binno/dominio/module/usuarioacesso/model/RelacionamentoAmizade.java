package com.binno.dominio.module.usuarioacesso.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "relacionamentos_amizades")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelacionamentoAmizade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate dataCriacao;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_owner", referencedColumnName = "id")
    private UsuarioAcesso owner;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_friend", referencedColumnName = "id")
    private UsuarioAcesso friend;
}

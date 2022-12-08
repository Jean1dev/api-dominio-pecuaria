package com.binno.dominio.module.usuarioacesso.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "convite_amizade")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConviteAmizade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String mensagem;
    @Builder.Default
    private Boolean aceito = false;
    @Builder.Default
    private Boolean rejeitado = false;
    private LocalDate dataSolicitacao;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_solicitante", referencedColumnName = "id")
    private UsuarioAcesso usuarioSolicitante;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_requisitado", referencedColumnName = "id")
    private UsuarioAcesso usuarioRequisitado;
}

package com.binno.dominio.module.usuarioacesso.model;

import com.binno.dominio.module.tenant.model.Tenant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuario_acesso")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String email;
    private String login;
    private String password;
    @Builder.Default
    private Boolean ativo = true;
    @Builder.Default
    private Boolean contaValidada = true;
    private String imagemPerfilUrl;
    private String sobrenome;
    private String numero;
    @Builder.Default
    private Boolean contaPublica = true;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL)
    private List<RelacionamentoAmizade> amigos;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant", referencedColumnName = "id")
    private Tenant tenant;
}

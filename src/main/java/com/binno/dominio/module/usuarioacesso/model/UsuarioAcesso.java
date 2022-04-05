package com.binno.dominio.module.usuarioacesso.model;

import com.binno.dominio.module.tenant.model.Tenant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

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

    private Boolean ativo = true;

    private Boolean contaValidada = true;

    private String imagemPerfilUrl;

    private String sobrenome;

    private String numero;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant", referencedColumnName = "id")
    private Tenant tenant;
}

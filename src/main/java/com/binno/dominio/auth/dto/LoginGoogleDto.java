package com.binno.dominio.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginGoogleDto {
    private String nome;
    private String email;
    private String uid;
    private String photoURL;
}

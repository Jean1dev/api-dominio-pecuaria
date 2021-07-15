package com.binno.dominio.auth;

import com.binno.dominio.module.usuarioacesso.model.UsuarioAcesso;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

@Service
public class UsuarioAutenticadoService {

    @Autowired
    private UsuarioAcessoRepository repository;

    public UsuarioAutenticado find(String login) {
        UsuarioAcesso usuarioAcesso = repository.findByLogin(login).orElseThrow();

        if (!usuarioAcesso.getAtivo())
            throw new ValidationException("Usuario inativo");

        return UsuarioAutenticado.builder()
                .id(usuarioAcesso.getId())
                .login(usuarioAcesso.getLogin())
                .password(usuarioAcesso.getPassword())
                .tenantId(usuarioAcesso.getTenant().getId())
                .build();
    }
}

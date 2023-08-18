package com.binno.dominio.auth;

import com.binno.dominio.module.usuarioacesso.model.UsuarioAcesso;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

@Service
@RequiredArgsConstructor
public class UsuarioAutenticadoService {

    private final UsuarioAcessoRepository repository;

    public UsuarioAutenticado find(String login) {
        UsuarioAcesso usuarioAcesso = repository.findByLogin(login).orElseThrow();

        if (!usuarioAcesso.getAtivo())
            throw new ValidationException("Usuario inativo");

        return UsuarioAutenticado.builder()
                .id(usuarioAcesso.getId())
                .login(usuarioAcesso.getLogin())
                .password(usuarioAcesso.getPassword())
                .tenantId(usuarioAcesso.getTenant().getId())
                .imagePerfilUrl(usuarioAcesso.getImagemPerfilUrl())
                .build();
    }
}

package com.binno.dominio.module.usuarioacesso.repository;

import com.binno.dominio.module.usuarioacesso.model.UsuarioAcesso;
import com.binno.dominio.shared.BasicRepository;

import java.util.Optional;

public interface UsuarioAcessoRepository extends BasicRepository<UsuarioAcesso, Integer> {

    Optional<UsuarioAcesso> findByLogin(String login);
}

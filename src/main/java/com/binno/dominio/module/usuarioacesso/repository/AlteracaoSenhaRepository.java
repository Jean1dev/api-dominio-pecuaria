package com.binno.dominio.module.usuarioacesso.repository;

import com.binno.dominio.module.usuarioacesso.model.AlteracaoSenha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlteracaoSenhaRepository extends JpaRepository<AlteracaoSenha, Integer> {

    Optional<AlteracaoSenha> findByChave(String chave);

    Optional<AlteracaoSenha> findByLogin(String login);
}

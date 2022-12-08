package com.binno.dominio.module.usuarioacesso.repository;

import com.binno.dominio.module.usuarioacesso.model.ConviteAmizade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConviteAmizadeRepository extends JpaRepository<ConviteAmizade, Integer> {

    Optional<ConviteAmizade> findByUsuarioRequisitadoIdAndUsuarioSolicitanteId(Integer requisitado, Integer solicitante);

    List<ConviteAmizade> findAllByUsuarioSolicitanteId(Integer idSolicitante);
}

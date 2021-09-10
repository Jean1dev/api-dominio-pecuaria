package com.binno.dominio.module.notificacao.repository;

import com.binno.dominio.module.notificacao.model.Notificacao;
import com.binno.dominio.shared.BasicRepository;

import java.util.List;

public interface NotificacaoRepository extends BasicRepository<Notificacao, Integer> {

    List<Notificacao> findAllByTenantIdOrderByIdDesc(Integer tenantId);
}

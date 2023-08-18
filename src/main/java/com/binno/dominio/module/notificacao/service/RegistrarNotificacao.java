package com.binno.dominio.module.notificacao.service;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.notificacao.model.Notificacao;
import com.binno.dominio.module.notificacao.repository.NotificacaoRepository;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.shared.RegraNegocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrarNotificacao implements RegraNegocioService<Notificacao, String> {

    private final NotificacaoRepository repository;

    private final AuthenticationHolder holder;

    @Override
    public Notificacao executar(String descricao) {
        return repository.save(Notificacao.builder()
                .descricao(descricao)
                .tenant(Tenant.of(holder.getTenantId()))
                .build());
    }

    public Notificacao executar(String descricao, Integer tenantId) {
        return repository.save(Notificacao.builder()
                .descricao(descricao)
                .tenant(Tenant.of(tenantId))
                .build());
    }
}

package com.binno.dominio.model.notificao.service;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.ContextFactory;
import com.binno.dominio.auth.TokenService;
import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.notificacao.model.Notificacao;
import com.binno.dominio.module.notificacao.service.RegistrarNotificacao;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("Registrar notificacao ")
public class RegistrarNotificacaoIT extends ApplicationConfigIT {

    @Autowired
    private RegistrarNotificacao service;

    @Autowired
    private AuthenticationHolder holder;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioAcessoRepository usuarioAcessoRepository;

    @Test
    @DisplayName("deve salvar uma notificacao no usuario certo")
    public void deveSalvar() {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        holder.setTenantId(tenant.getId());

        String texto = "NOTIFICAO DE TESTE";
        Notificacao salvo = service.executar(texto);
        Assertions.assertEquals(texto, salvo.getDescricao());
        Assertions.assertEquals(tenant.getId(), salvo.getTenant().getId());
    }
}

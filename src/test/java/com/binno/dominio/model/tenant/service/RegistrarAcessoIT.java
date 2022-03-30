package com.binno.dominio.model.tenant.service;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.auth.TokenService;
import com.binno.dominio.auth.UsuarioAutenticado;
import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.factory.ContextFactory;
import com.binno.dominio.module.tenant.model.Acessos;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.tenant.service.RegistrarAcesso;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("Registrar Acessos Test")
public class RegistrarAcessoIT extends ApplicationConfigIT {

    @Autowired
    private RegistrarAcesso registrarAcesso;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioAcessoRepository usuarioAcessoRepository;

    @Autowired
    private AuthenticationHolder holder;

    @Test
    @DisplayName("deve registar um acesso e retornar")
    public void deveSalvar() {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        holder.setTenantId(tenant.getId());
        String login = "um-login-qualquer";
        UsuarioAutenticado usuarioAutenticado = contextFactory.getUsuarioAutenticado();
        usuarioAutenticado.setLogin(login);
        Acessos acesso = registrarAcesso.executar(usuarioAutenticado);
        Assertions.assertNotNull(acesso);
        Assertions.assertEquals(login, acesso.getLogin());
        Assertions.assertNotNull(acesso.getDataHoraAcesso());
    }

    @Test
    @DisplayName("nao deve salvar acesso com login nullo")
    public void naoDeveSalvar() {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        holder.setTenantId(tenant.getId());
        Assertions.assertThrows(Exception.class, () -> registrarAcesso.executar(null));
    }
}

package com.binno.dominio.factory;

import com.binno.dominio.auth.TokenService;
import com.binno.dominio.auth.UsuarioAutenticado;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.model.UsuarioAcesso;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import org.springframework.security.core.Authentication;

import java.util.Objects;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContextFactory {

    private final TenantRepository tenantRepository;

    private final TokenService tokenService;

    private final UsuarioAcessoRepository repository;

    private Tenant tenant;

    private UsuarioAutenticado usuario;

    public ContextFactory(TenantRepository tenantRepository, TokenService tokenService, UsuarioAcessoRepository repository) {
        this.tenantRepository = tenantRepository;
        this.tokenService = tokenService;
        this.repository = repository;
    }

    public Tenant umTenantSalvo() {
        this.tenant = tenantRepository.save(Tenant.builder()
                .ativo(true)
                .nome("teste")
                .build());

        return tenant;
    }

    public String gerarToken() {
        Authentication authentication = mock(Authentication.class);
        String login = UUID.randomUUID().toString();
        verificarSeJaExisteTenant();
        repository.save(UsuarioAcesso.builder()
                .login(login)
                .password("senha")
                .tenant(tenant)
                .ativo(true)
                .build());

        usuario = UsuarioAutenticado.builder()
                .tenantId(tenant.getId())
                .password("senha")
                .login(login)
                .build();

        when(authentication.getPrincipal()).thenReturn(usuario);

        return tokenService.gerarToken(authentication);
    }

    private void verificarSeJaExisteTenant() {
        if (Objects.isNull(tenant)) {
            tenant = umTenantSalvo();
        }
    }

    public UsuarioAutenticado getUsuarioAutenticado() {
        return usuario;
    }
}

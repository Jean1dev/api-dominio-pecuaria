package com.binno.dominio.model.usuario;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.auth.TokenService;
import com.binno.dominio.factory.ContextFactory;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.api.dto.AlterarUsuarioDto;
import com.binno.dominio.module.usuarioacesso.model.UsuarioAcesso;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import com.binno.dominio.module.usuarioacesso.service.AlterarDadosUsuarioService;
import com.binno.dominio.provider.mail.MailProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.ValidationException;

@DisplayName("AlterarDadosUsuarioService")
public class AlterarDadosUsuarioServiceIT extends ApplicationConfigIT {

    @Autowired
    private AlterarDadosUsuarioService service;

    @Autowired
    private UsuarioAcessoRepository repository;

    @MockBean
    private MailProvider mailProvider;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioAcessoRepository usuarioAcessoRepository;

    @Test
    @DisplayName("nao deve alterar o usuario pq login já existe")
    public void naoDeveAlterarUsuario() {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        String login = "jucas carlos";
        String loginDoSegundoUsuario = "joao gilberto";
        UsuarioAcesso usuarioAcesso = repository.save(UsuarioAcesso.builder()
                .login(login)
                .password("password")
                .email("email")
                .tenant(tenant)
                .build());

        repository.save(UsuarioAcesso.builder()
                .login(loginDoSegundoUsuario)
                .password("password")
                .email("email")
                .tenant(tenant)
                .build());

        AlterarUsuarioDto usuarioDto = AlterarUsuarioDto.builder()
                .id(usuarioAcesso.getId())
                .login(loginDoSegundoUsuario)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> service.executar(usuarioDto), "Já existe um usuario utilizando esse Login");

        repository.deleteAll();
    }
}

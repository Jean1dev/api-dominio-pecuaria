package com.binno.dominio.model.usuario;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.auth.TokenService;
import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.factory.ContextFactory;
import com.binno.dominio.module.notificacao.service.RegistrarNotificacao;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.api.dto.AlterarUsuarioDto;
import com.binno.dominio.module.usuarioacesso.model.AlteracaoSenha;
import com.binno.dominio.module.usuarioacesso.model.UsuarioAcesso;
import com.binno.dominio.module.usuarioacesso.repository.AlteracaoSenhaRepository;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import com.binno.dominio.module.usuarioacesso.service.AlterarDadosUsuarioService;
import com.binno.dominio.module.usuarioacesso.service.UsuarioAcessoService;
import com.binno.dominio.provider.mail.MailProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@DisplayName("UsuarioAcessoService Test")
public class UsuarioAcessoServiceIT extends ApplicationConfigIT {

    @Autowired
    private UsuarioAcessoService service;
    @Autowired
    private UsuarioAcessoRepository repository;
    @Autowired
    private AuthenticationHolder holder;
    @MockBean
    private MailProvider mailProvider;
    @MockBean
    private AlteracaoSenhaRepository alteracaoSenhaRepository;
    @MockBean
    private RegistrarNotificacao registrarNotificacao;
    @MockBean
    private AlterarDadosUsuarioService alterarDadosUsuarioService;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsuarioAcessoRepository usuarioAcessoRepository;

    @Test
    @DisplayName("Nao deve alterar a senha pois o prazo esta expirado")
    public void naoDeveAlterarSenha() {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        String login = UUID.randomUUID().toString();

        UsuarioAcesso usuarioAcesso = repository.save(UsuarioAcesso.builder()
                .login(login)
                .password("password")
                .email("email")
                .tenant(tenant)
                .build());

        LocalDateTime horaCriacao = LocalDateTime.now().minus(Duration.ofHours(9));
        AlteracaoSenha alteracaoSenha = AlteracaoSenha.builder()
                .chave(login)
                .dataHoraCriacao(horaCriacao)
                .timeExpiracao(Duration.ofHours(8))
                .email(usuarioAcesso.getEmail())
                .login(login)
                .build();

        Mockito.when(alteracaoSenhaRepository.findByChave(eq(login))).thenReturn(java.util.Optional.ofNullable(alteracaoSenha));

        boolean senhaAlterada = service.alterarSenha(login, "novaSenha");
        Assertions.assertFalse(senhaAlterada);
        Mockito.verify(alterarDadosUsuarioService, Mockito.never()).executar(any(AlterarUsuarioDto.class));
        Mockito.verify(alteracaoSenhaRepository, Mockito.times(1)).delete(eq(alteracaoSenha));
    }

    @Test
    @DisplayName("Deve alterar a senha do usuario")
    public void deveAlterarASenha() {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        String login = UUID.randomUUID().toString();

        UsuarioAcesso usuarioAcesso = repository.save(UsuarioAcesso.builder()
                .login(login)
                .password("password")
                .email("email")
                .tenant(tenant)
                .build());

        AlteracaoSenha alteracaoSenha = AlteracaoSenha.builder()
                .chave(login)
                .dataHoraCriacao(LocalDateTime.now())
                .timeExpiracao(Duration.ofHours(8))
                .email(usuarioAcesso.getEmail())
                .login(login)
                .build();

        Mockito.when(alteracaoSenhaRepository.findByChave(eq(login))).thenReturn(java.util.Optional.ofNullable(alteracaoSenha));

        boolean senhaAlterada = service.alterarSenha(login, "novaSenha");
        Assertions.assertTrue(senhaAlterada);
        Mockito.verify(alterarDadosUsuarioService, Mockito.times(1)).executar(any(AlterarUsuarioDto.class));
        Mockito.verify(alteracaoSenhaRepository, Mockito.times(1)).delete(eq(alteracaoSenha));
    }
}

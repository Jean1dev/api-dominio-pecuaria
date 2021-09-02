package com.binno.dominio.model.notificacao.api;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.auth.TokenService;
import com.binno.dominio.factory.ContextFactory;
import com.binno.dominio.module.notificacao.api.NotificacaoController;
import com.binno.dominio.module.notificacao.model.Notificacao;
import com.binno.dominio.module.notificacao.repository.NotificacaoRepository;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Api Notificacao")
public class NotificacaoApiIT extends ApplicationConfigIT {

    private static final String PATH = "/" + NotificacaoController.PATH;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioAcessoRepository usuarioAcessoRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificacaoRepository repository;

    @Test
    @DisplayName("deve marcar uma notificacao como lida")
    public void deveMarcarUma() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();

        Notificacao notificacao = repository.save(Notificacao.builder()
                .descricao("teste")
                .tenant(Tenant.of(tenant.getId()))
                .build());

        String token = contextFactory.gerarToken();

        mockMvc.perform(request(HttpMethod.PUT, PATH + "/marcar-como-lida/" + notificacao.getId())
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isAccepted());

        List<Notificacao> allByTenantId = repository.findAllByTenantId(tenant.getId());
        Assertions.assertTrue(allByTenantId.isEmpty());
    }

    @Test
    @DisplayName("deve marcar a notificacao correta como lida como lida")
    public void deveMarcarUmaEntreDuas() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();

        Notificacao notificacao = repository.save(Notificacao.builder()
                .descricao("teste")
                .tenant(Tenant.of(tenant.getId()))
                .build());

        Notificacao notificacao1 = repository.save(Notificacao.builder()
                .descricao("outra")
                .tenant(Tenant.of(tenant.getId()))
                .build());

        String token = contextFactory.gerarToken();

        mockMvc.perform(request(HttpMethod.PUT, PATH + "/marcar-como-lida/" + notificacao.getId())
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isAccepted());

        List<Notificacao> allByTenantId = repository.findAllByTenantId(tenant.getId());
        Assertions.assertEquals(1, allByTenantId.size());
        Assertions.assertEquals(notificacao1.getDescricao(), allByTenantId.get(0).getDescricao());
    }

    @Test
    @DisplayName("deve marcar todas as notificacoes como lida")
    public void deveMarcarTodasComoLisda() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();

        Notificacao notificacao = repository.save(Notificacao.builder()
                .descricao("teste")
                .tenant(Tenant.of(tenant.getId()))
                .build());

        Notificacao notificacao1 = repository.save(Notificacao.builder()
                .descricao("outra")
                .tenant(Tenant.of(tenant.getId()))
                .build());

        String token = contextFactory.gerarToken();

        mockMvc.perform(request(HttpMethod.POST, PATH + "/marcar-todos-como-lidas")
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isAccepted());

        List<Notificacao> allByTenantId = repository.findAllByTenantId(tenant.getId());
        Assertions.assertTrue(allByTenantId.isEmpty());
    }
}

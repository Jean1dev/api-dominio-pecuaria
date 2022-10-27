package com.binno.dominio.model.comercializacao.api;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.auth.TokenService;
import com.binno.dominio.factory.ContextFactory;
import com.binno.dominio.module.comercializacao.api.ComercializacaoController;
import com.binno.dominio.module.comercializacao.http.dto.CambioDto;
import com.binno.dominio.module.comercializacao.service.ComercializacaoService;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ComercializacaoController")
public class ComercializacaoControllerTest extends ApplicationConfigIT {

    public static final String PATH = "/" + ComercializacaoController.PATH;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioAcessoRepository usuarioAcessoRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComercializacaoService comercializacaoService;

    @DisplayName("Deve pegar a ultima cotacao")
    @Test
    public void devePegarUltimaCotacao() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        String token = contextFactory.gerarToken();

        CambioDto cambio = new CambioDto();
        cambio.setBid(2.0);

        Mockito.when(comercializacaoService.getUltimaCotacao()).thenReturn(cambio);

        ResultActions resultActions = mockMvc.perform(request(HttpMethod.GET, PATH + "/ultima")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$.bid", is(cambio.getBid())));
    }

    @Test
    @DisplayName("deve confirmar a lista de comercializacao")
    public void deveConfirmar() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        String token = contextFactory.gerarToken();

        mockMvc.perform(request(HttpMethod.POST, PATH + "/confirmar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }


}

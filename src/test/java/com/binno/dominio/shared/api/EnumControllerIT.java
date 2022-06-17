package com.binno.dominio.shared.api;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.auth.TokenService;
import com.binno.dominio.factory.ContextFactory;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Enum Controller Test")
public class EnumControllerIT extends ApplicationConfigIT {

    private static final String PATH = "/" + EnumController.PATH;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioAcessoRepository usuarioAcessoRepository;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Deve retornar todos os enums")
    @Test
    public void deveBuscarEnum() throws Exception {
        String token = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository)
                .gerarToken();

        ResultActions resultActions = mockMvc.perform(request(HttpMethod.GET, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$.StatusAgendamento", notNullValue()))
                .andExpect(jsonPath("$.RacaAnimal", notNullValue()))
                .andExpect(jsonPath("$.EstadoAtual", notNullValue()))
                .andExpect(jsonPath("$.PeriodoDia", notNullValue()))
                .andExpect(jsonPath("$.TipoMetragem", notNullValue()));
    }
}

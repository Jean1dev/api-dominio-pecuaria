package com.binno.dominio.model.veterinaria.api;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.auth.TokenService;
import com.binno.dominio.factory.ContextFactory;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import com.binno.dominio.module.veterinaria.api.AgendamentoVeterinarioController;
import com.binno.dominio.module.veterinaria.api.dto.CriarAgendamentoDto;
import com.binno.dominio.module.veterinaria.model.PeriodoDia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.binno.dominio.util.TestUtils.objectToJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("AgendamentoVeterinarioController Test")
public class AgendamentoVeterinarioControllerIT extends ApplicationConfigIT {

    private static final String PATH = "/" + AgendamentoVeterinarioController.PATH;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioAcessoRepository usuarioAcessoRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("deve criar uma solicitacao de agendamento")
    public void deveCriar() throws Exception {
        CriarAgendamentoDto dto = CriarAgendamentoDto.builder()
                .dataAgendamento(LocalDate.now().plusDays(1))
                .periodoDia(PeriodoDia.MANHA)
                .build();

        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        contextFactory.umTenantSalvo();
        String token = contextFactory.gerarToken();

        String payload = objectToJson(dto);
        mockMvc.perform(request(HttpMethod.POST, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Nao deve solicitar agendamento pois o dia agendado ja passou")
    public void naoDeveSolicitar() throws Exception {
        CriarAgendamentoDto dto = CriarAgendamentoDto.builder()
                .dataAgendamento(LocalDate.now().minusDays(1))
                .periodoDia(PeriodoDia.MANHA)
                .build();

        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        contextFactory.umTenantSalvo();
        String token = contextFactory.gerarToken();

        String payload = objectToJson(dto);
        mockMvc.perform(request(HttpMethod.POST, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }
}

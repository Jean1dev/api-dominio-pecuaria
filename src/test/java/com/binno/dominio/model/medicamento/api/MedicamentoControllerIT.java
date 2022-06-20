package com.binno.dominio.model.medicamento.api;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.auth.TokenService;
import com.binno.dominio.factory.ContextFactory;
import com.binno.dominio.module.medicamento.api.MedicamentoController;
import com.binno.dominio.module.medicamento.api.dto.MedicamentoDto;
import com.binno.dominio.module.medicamento.model.EstadoMedicamento;
import com.binno.dominio.module.medicamento.model.Medicamento;
import com.binno.dominio.module.medicamento.repository.MedicamentoRepository;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.binno.dominio.factory.MedicamentoFactory.umMedicamento;
import static com.binno.dominio.util.TestUtils.objectToJson;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Medicamento Controller Test")
public class MedicamentoControllerIT extends ApplicationConfigIT {

    private static final String PATH = "/" + MedicamentoController.PATH;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioAcessoRepository usuarioAcessoRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicamentoRepository repository;

    @Test
    @DisplayName("Deve salvar um medicamento")
    public void deveSalvar() throws Exception {
        MedicamentoDto medicamentoDto = MedicamentoDto.builder()
                .nome("Farmaceutico")
                .descricao("remedio")
                .build();

        String token = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository).gerarToken();
        mockMvc.perform(request(HttpMethod.POST, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(medicamentoDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve trazer a pagina de medicamentos")
    public void deveBuscarPagina() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        Medicamento medicamento = repository.save(umMedicamento(tenant));

        String token = contextFactory.gerarToken();

        ResultActions resultActions = mockMvc.perform(request(HttpMethod.GET, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$.content.length()", is(1)))
                .andExpect(jsonPath("$.content..estadoMedicamento", hasItems(List.of(EstadoMedicamento.VENCIDO.name()).toArray())))
                .andExpect(jsonPath("$.content..nome", hasItems(List.of(medicamento.getNome()).toArray())));
    }

    @Test
    @DisplayName("Deve trazer a listagem de medicamentos")
    public void deveBuscarListagem() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        Medicamento medicamento = repository.save(umMedicamento(tenant));

        String token = contextFactory.gerarToken();

        ResultActions resultActions = mockMvc.perform(request(HttpMethod.GET, PATH + "/" + "listagem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$..estadoMedicamento", hasItems(List.of(EstadoMedicamento.VENCIDO.name()).toArray())))
                .andExpect(jsonPath("$..nome", hasItems(List.of(medicamento.getNome()).toArray())));
    }
}

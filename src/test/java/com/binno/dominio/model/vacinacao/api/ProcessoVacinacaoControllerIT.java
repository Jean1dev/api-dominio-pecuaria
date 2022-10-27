package com.binno.dominio.model.vacinacao.api;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.auth.TokenService;
import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.factory.ContextFactory;
import com.binno.dominio.factory.FazendaFactory;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.fazenda.model.Fazenda;
import com.binno.dominio.module.fazenda.repository.FazendaRepository;
import com.binno.dominio.module.medicamento.model.Medicamento;
import com.binno.dominio.module.medicamento.repository.MedicamentoRepository;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import com.binno.dominio.module.vacinacao.api.ProcessoVacinacaoController;
import com.binno.dominio.module.vacinacao.api.dto.ProcessarVacinacaoDto;
import com.binno.dominio.module.vacinacao.model.ProcessoVacinacao;
import com.binno.dominio.module.vacinacao.repository.ProcessoVacinacaoRepository;
import com.binno.dominio.module.vacinacao.service.ProcessarVacinacao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.binno.dominio.factory.AnimalFactory.persistir;
import static com.binno.dominio.factory.AnimalFactory.umAnimalCompleto;
import static com.binno.dominio.factory.FazendaFactory.umaFazendaCompleta;
import static com.binno.dominio.factory.MedicamentoFactory.umMedicamento;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ProcessoVacinacaoController")
public class ProcessoVacinacaoControllerIT extends ApplicationConfigIT {

    public static final String PATH = "/" + ProcessoVacinacaoController.PATH;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioAcessoRepository usuarioAcessoRepository;

    @MockBean
    private ProcessarVacinacao processarVacinacao;

    @MockBean
    private ProcessoVacinacaoRepository repository;

    @Autowired
    private AuthenticationHolder holder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private FazendaRepository fazendaRepository;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Test
    @DisplayName("deve processar um pedido de vacinacao")
    public void deveProcessarUmPedidoDeVacinacao() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        String token = contextFactory.gerarToken();

        Medicamento medicamento = medicamentoRepository.save(umMedicamento(tenant));
        Fazenda fazenda = FazendaFactory.persistir(fazendaRepository, umaFazendaCompleta(tenant));

        Animal animal = persistir(animalRepository, umAnimalCompleto(tenant, fazenda));

        List<Integer> listagemAnimais = Collections.singletonList(animal.getId());
        ProcessarVacinacaoDto dto = new ProcessarVacinacaoDto(listagemAnimais, medicamento.getId());

        mockMvc.perform(request(HttpMethod.POST, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isAccepted());
    }

    @Test
    @DisplayName("deve retornar uma pagina de vacinacao")
    public void deveRetornarUmaPaginaVacinacao() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        String token = contextFactory.gerarToken();

        ProcessoVacinacao processoVacinacao = Mockito.mock(ProcessoVacinacao.class);
        Mockito.when(processoVacinacao.getMedicamento()).thenReturn(umMedicamento(tenant));
        PageImpl<ProcessoVacinacao> page = new PageImpl<>(Collections.singletonList(processoVacinacao), PageRequest.of(0, 50), 1);

        Mockito.when(repository.findAllByTenantId(ArgumentMatchers.any(Pageable.class), ArgumentMatchers.eq(tenant.getId()))).thenReturn(page);

        ResultActions resultActions = mockMvc.perform(request(HttpMethod.GET, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("size", String.valueOf(50))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$.content.length()", is(1)));
    }

}

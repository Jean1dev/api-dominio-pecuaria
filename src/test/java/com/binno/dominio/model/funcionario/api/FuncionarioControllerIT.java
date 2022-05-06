package com.binno.dominio.model.funcionario.api;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.auth.TokenService;
import com.binno.dominio.factory.ContextFactory;
import com.binno.dominio.factory.FazendaFactory;
import com.binno.dominio.module.fazenda.api.dto.FazendaAgregadaDto;
import com.binno.dominio.module.fazenda.model.Fazenda;
import com.binno.dominio.module.fazenda.repository.FazendaRepository;
import com.binno.dominio.module.funcionario.api.FuncionarioController;
import com.binno.dominio.module.funcionario.api.dto.FuncionarioDto;
import com.binno.dominio.module.funcionario.model.Funcionario;
import com.binno.dominio.module.funcionario.repository.FuncionarioRepository;
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

import static com.binno.dominio.factory.FuncionarioFactory.umFuncionarioCompleto;
import static com.binno.dominio.util.TestUtils.objectToJson;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("FuncionarioController")
public class FuncionarioControllerIT extends ApplicationConfigIT {

    private static final String PATH = "/" + FuncionarioController.PATH;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioAcessoRepository usuarioAcessoRepository;

    @Autowired
    private FazendaRepository fazendaRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FuncionarioRepository repository;

    @Test
    @DisplayName("Deve criar um funcionario")
    public void deveCriar() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        Fazenda fazenda = FazendaFactory.persistir(fazendaRepository, FazendaFactory.umaFazendaCompleta(tenant));
        String token = contextFactory.gerarToken();

        FuncionarioDto dto = FuncionarioDto.builder()
                .cargo("Rancheiro")
                .cpf("Cpf")
                .rg("Rg")
                .nome("Juca Rancheiro")
                .fazenda(FazendaAgregadaDto.builder().id(fazenda.getId()).build())
                .build();

        mockMvc.perform(request(HttpMethod.POST, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(dto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve atualizar um Funcionario")
    public void atualizar() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        Fazenda fazenda = FazendaFactory.persistir(fazendaRepository, FazendaFactory.umaFazendaCompleta(tenant));
        String token = contextFactory.gerarToken();
        Funcionario funcionario = repository.save(umFuncionarioCompleto(tenant, fazenda));

        FuncionarioDto dto = FuncionarioDto.builder()
                .id(funcionario.getId())
                .fazenda(FazendaAgregadaDto.builder().id(fazenda.getId()).build())
                .nome("Atualizado")
                .build();

        mockMvc.perform(request(HttpMethod.PUT, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(dto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve excluir funcionario")
    public void excluir() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        Fazenda fazenda = FazendaFactory.persistir(fazendaRepository, FazendaFactory.umaFazendaCompleta(tenant));
        String token = contextFactory.gerarToken();
        Funcionario funcionario = repository.save(umFuncionarioCompleto(tenant, fazenda));

        mockMvc.perform(request(HttpMethod.DELETE, PATH + "/" + funcionario.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve recuperar o funcionario")
    public void deveRecuperar() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        Fazenda fazenda = FazendaFactory.persistir(fazendaRepository, FazendaFactory.umaFazendaCompleta(tenant));
        String token = contextFactory.gerarToken();
        Funcionario funcionario = repository.save(umFuncionarioCompleto(tenant, fazenda));

        ResultActions resultActions = mockMvc.perform(request(HttpMethod.GET, PATH + "/" + funcionario.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$.id", is(funcionario.getId())))
                .andExpect(jsonPath("$.nome", is(funcionario.getNome())));
    }

    @Test
    @DisplayName("deve buscar o funcionario com o nome certo")
    public void deveBuscarCorreto() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        Fazenda fazenda = FazendaFactory.persistir(fazendaRepository, FazendaFactory.umaFazendaCompleta(tenant));
        String token = contextFactory.gerarToken();
        repository.save(umFuncionarioCompleto(tenant, fazenda));
        Funcionario outroFuncionario = umFuncionarioCompleto(tenant, fazenda);
        outroFuncionario.setNome("Renata");
        repository.save(outroFuncionario);

        ResultActions resultActions = mockMvc.perform(request(HttpMethod.GET, PATH)
                        .param("nome", "Renata")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$.content.length()", is(1)))
                .andExpect(jsonPath("$.content..nome", hasItems(List.of("Renata").toArray())));
    }
}

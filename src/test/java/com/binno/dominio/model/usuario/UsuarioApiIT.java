package com.binno.dominio.model.usuario;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.auth.TokenService;
import com.binno.dominio.auth.UsuarioAutenticado;
import com.binno.dominio.factory.ContextFactory;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.api.UsuarioAcessoController;
import com.binno.dominio.module.usuarioacesso.api.dto.AlterarUsuarioDto;
import com.binno.dominio.module.usuarioacesso.api.dto.UsuarioAcessoDto;
import com.binno.dominio.module.usuarioacesso.model.UsuarioAcesso;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import com.binno.dominio.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("usuario acesso Api")
public class UsuarioApiIT extends ApplicationConfigIT {

    private static final String PATH = "/" + UsuarioAcessoController.PATH;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioAcessoRepository usuarioAcessoRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioAcessoRepository repository;

    @Test
    @DisplayName("deve buscar meus usuarios")
    public void deveBuscarMeusUsuarios() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        String token = contextFactory.gerarToken();
        UsuarioAutenticado usuarioAutenticado = contextFactory.getUsuarioAutenticado();

        UsuarioAcesso usuarioAcesso = repository.findByLogin(usuarioAutenticado.getLogin()).orElseThrow();
        String login = usuarioAcesso.getLogin();

        ResultActions resultActions = mockMvc.perform(request(HttpMethod.GET, PATH + "/meus-usuarios")
                        .header("authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$.content.length()", is(1)))
                .andExpect(jsonPath("$.content..login", hasItems(List.of(login).toArray())));
    }

    @Test
    @DisplayName("deve solicitar alteracao de senha para um usuario criado")
    public void deveSolicitarAlteracaoDeSenha() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        String token = contextFactory.gerarToken();
        UsuarioAutenticado usuarioAutenticado = contextFactory.getUsuarioAutenticado();
        String login = UUID.randomUUID().toString();
        UsuarioAcessoDto roger = UsuarioAcessoDto.builder()
                .login(login)
                .password("roger")
                .email("roger_@gmail.com")
                .build();

        mockMvc.perform(request(HttpMethod.POST, PATH + "/criar-para-tenant-existente")
                        .header("authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(roger)))
                .andExpect(status().isAccepted());

        Map<String, String> payload = Map.of("login", roger.getLogin());

        mockMvc.perform(request(HttpMethod.POST, PATH + "/solicitar-alteracao-senha")
                        .header("authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(payload)))
                .andExpect(status().isAccepted());
    }

    @Test
    @DisplayName("deve criar um usuario para o mesmo tenant")
    public void deveCriarUsuarioParaMesmoTenat() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        String token = contextFactory.gerarToken();
        UsuarioAutenticado usuarioAutenticado = contextFactory.getUsuarioAutenticado();

        UsuarioAcesso usuarioAcesso = repository.findByLogin(usuarioAutenticado.getLogin()).orElseThrow();

        String login = UUID.randomUUID() + "login-de-teste";
        UsuarioAcessoDto roger = UsuarioAcessoDto.builder()
                .login(login)
                .password("roger")
                .email("roger@gmail.com")
                .build();

        mockMvc.perform(request(HttpMethod.POST, PATH + "/criar-para-tenant-existente")
                        .header("authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(roger)))
                .andExpect(status().isAccepted());

        List<UsuarioAcesso> usuarioAcessos = repository.findAllByTenantId(contextFactory.getTenant().getId());
        Assertions.assertEquals(2, usuarioAcessos.size());
        UsuarioAcesso rogerEncontrado = usuarioAcessos.stream().filter(acesso -> acesso.getLogin().equals(login)).findFirst().orElseThrow();
        Assertions.assertNotNull(rogerEncontrado);
    }

    @Test
    @DisplayName("deve criar um usuario")
    public void deveCriarUsuario() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();

        String login = UUID.randomUUID() + "login-de-teste";
        UsuarioAcessoDto juqinha = UsuarioAcessoDto.builder()
                .login(login)
                .password("juqinha")
                .email("jeanlucafp@gmail.com")
                .build();

        mockMvc.perform(request(HttpMethod.POST, PATH + "/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(juqinha)))
                .andExpect(status().isAccepted());

        UsuarioAcesso usuarioAcesso = repository.findByLogin(login).orElseThrow();
        Assertions.assertNotNull(usuarioAcesso);
    }

    @Test
    @DisplayName("deve alterar um usuario")
    public void deveAlterarUsuario() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        String token = contextFactory.gerarToken();
        UsuarioAutenticado usuarioAutenticado = contextFactory.getUsuarioAutenticado();

        UsuarioAcesso usuarioAcesso = repository.findByLogin(usuarioAutenticado.getLogin()).orElseThrow();

        String login = UUID.randomUUID().toString();
        AlterarUsuarioDto alterarUsuarioDto = AlterarUsuarioDto.builder()
                .id(usuarioAcesso.getId())
                .nome("novo nome")
                .imagemPerfilUrl("url")
                .numero("numero de telefone")
                .password("nova senha")
                .email("jeanluca_fp@hotmail.com")
                .sobrenome("novo sobrenome")
                .login(login)
                .build();

        mockMvc.perform(request(HttpMethod.PUT, PATH)
                        .header("authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(alterarUsuarioDto)))
                .andExpect(status().isAccepted());

        UsuarioAcesso usuarioAlterado = repository.findById(usuarioAcesso.getId()).orElseThrow();
        Assertions.assertEquals("novo nome", usuarioAlterado.getNome());
        Assertions.assertEquals("novo sobrenome", usuarioAlterado.getSobrenome());
        Assertions.assertEquals("jeanluca_fp@hotmail.com", usuarioAlterado.getEmail());
        Assertions.assertEquals(login, usuarioAlterado.getLogin());
    }
}

package com.binno.dominio.model.animal.api;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.auth.TokenService;
import com.binno.dominio.factory.ContextFactory;
import com.binno.dominio.factory.FazendaFactory;
import com.binno.dominio.module.animal.api.AnimalController;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.model.PesoAnimal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.animal.repository.PesoAnimalRepository;
import com.binno.dominio.module.fazenda.repository.FazendaRepository;
import com.binno.dominio.module.imagem.model.Imagem;
import com.binno.dominio.module.imagem.repository.ImagemRepository;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.binno.dominio.factory.AnimalFactory.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Animal REST API")
public class AnimalApiIT extends ApplicationConfigIT {

    private static final String PATH = "/" + AnimalController.PATH;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioAcessoRepository usuarioAcessoRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private ImagemRepository imagemRepository;

    @Autowired
    private PesoAnimalRepository pesoAnimalRepository;

    @Autowired
    private FazendaRepository fazendaRepository;

    @Test
    @DisplayName("deve excluir um animal e seus dependentes")
    public void deveExcluirAnimalESeusDependentes() throws Exception {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        Animal animalSalvo = umAnimalCompleto(tenant, FazendaFactory.persistir(fazendaRepository, FazendaFactory.umaFazendaCompleta(tenant)));
        Animal animal = persistir(animalRepository, animalSalvo);
        Imagem imagem = imagemRepository.save(umaImagem(animal));
        PesoAnimal pesoAnimal = pesoAnimalRepository.save(umPesoAnimal(animal));

        String token = contextFactory.gerarToken();
        mockMvc.perform(request(HttpMethod.DELETE, PATH + "/" + animal.getId())
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());

        Optional<Imagem> optionalImagem = imagemRepository.findById(imagem.getId());
        Optional<PesoAnimal> pesoAnimalOptional = pesoAnimalRepository.findById(pesoAnimal.getId());
        Optional<Animal> optionalAnimal = animalRepository.findById(animal.getId());

        Assertions.assertTrue(optionalImagem.isEmpty());
        Assertions.assertTrue(pesoAnimalOptional.isEmpty());
        Assertions.assertTrue(optionalAnimal.isEmpty());
    }
}

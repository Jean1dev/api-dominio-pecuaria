package com.binno.dominio.model.animal.service;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.auth.TokenService;
import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.factory.ContextFactory;
import com.binno.dominio.factory.FazendaFactory;
import com.binno.dominio.module.animal.api.dto.TransferirAnimalEntreFazendaDto;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.animal.service.TransferirAnimalEntreFazendasService;
import com.binno.dominio.module.fazenda.model.Fazenda;
import com.binno.dominio.module.fazenda.repository.FazendaRepository;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.binno.dominio.factory.AnimalFactory.persistir;
import static com.binno.dominio.factory.AnimalFactory.umAnimalCompleto;

@DisplayName("TransferirAnimalEntreFazendasService")
public class TransferirAnimalEntreFazendasServiceIT extends ApplicationConfigIT {

    @Autowired
    private AuthenticationHolder holder;

    @Autowired
    private TransferirAnimalEntreFazendasService service;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioAcessoRepository usuarioAcessoRepository;

    @Autowired
    private FazendaRepository fazendaRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Test
    @DisplayName("Deve transferir apenas dois animais")
    public void deveTransferir2Animais() throws InterruptedException {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        Fazenda fazendaOrigem = FazendaFactory.persistir(fazendaRepository, FazendaFactory.umaFazendaCompleta(tenant));
        Fazenda fazendaDestino = FazendaFactory.persistir(fazendaRepository, FazendaFactory.umaFazendaCompleta(tenant));
        Animal animal1 = persistir(animalRepository, umAnimalCompleto(tenant, fazendaOrigem));
        Animal animal2 = persistir(animalRepository, umAnimalCompleto(tenant, fazendaOrigem));
        Animal animal3 = persistir(animalRepository, umAnimalCompleto(tenant, fazendaOrigem));

        holder.setTenantId(tenant.getId());
        TransferirAnimalEntreFazendaDto dto = TransferirAnimalEntreFazendaDto.builder()
                .fazendaOrigemId(fazendaOrigem.getId())
                .fazendaDestinoId(fazendaDestino.getId())
                .animaisSelecionadosId(Arrays.asList(animal1.getId(), animal2.getId()))
                .todosOsAnimais(false)
                .build();

        service.executar(dto);
        executor.getThreadPoolExecutor().awaitTermination(2, TimeUnit.SECONDS);

        int totalFazendaDestino = animalRepository.findAllByFazendaId(fazendaDestino.getId()).size();
        int totalFazendaOrigem = animalRepository.findAllByFazendaId(fazendaOrigem.getId()).size();
        Assertions.assertEquals(1, totalFazendaOrigem);
        Assertions.assertEquals(2, totalFazendaDestino);
    }

    @Test
    @DisplayName("Deve transferir todos os animais")
    public void deveTransferirTodos() throws InterruptedException {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        Fazenda fazendaOrigem = FazendaFactory.persistir(fazendaRepository, FazendaFactory.umaFazendaCompleta(tenant));
        Fazenda fazendaDestino = FazendaFactory.persistir(fazendaRepository, FazendaFactory.umaFazendaCompleta(tenant));
        Animal animal1 = persistir(animalRepository, umAnimalCompleto(tenant, fazendaOrigem));
        Animal animal2 = persistir(animalRepository, umAnimalCompleto(tenant, fazendaOrigem));
        Animal animal3 = persistir(animalRepository, umAnimalCompleto(tenant, fazendaOrigem));

        holder.setTenantId(tenant.getId());
        TransferirAnimalEntreFazendaDto dto = TransferirAnimalEntreFazendaDto.builder()
                .fazendaOrigemId(fazendaOrigem.getId())
                .fazendaDestinoId(fazendaDestino.getId())
                .todosOsAnimais(true)
                .build();

        service.executar(dto);
        executor.getThreadPoolExecutor().awaitTermination(2, TimeUnit.SECONDS);

        int totalFazendaDestino = animalRepository.findAllByFazendaId(fazendaDestino.getId()).size();
        int totalFazendaOrigem = animalRepository.findAllByFazendaId(fazendaOrigem.getId()).size();
        Assertions.assertEquals(0, totalFazendaOrigem);
        Assertions.assertEquals(3, totalFazendaDestino);
    }
}

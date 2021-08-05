package com.binno.dominio.model.animal.service;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.ContextFactory;
import com.binno.dominio.auth.TokenService;
import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.model.animal.AnimalFactory;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.animal.service.AnimalConsultasService;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("Consultas Animal Service")
public class AnimalConsultasServiceIT extends ApplicationConfigIT {

    @Autowired
    private AnimalConsultasService service;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private AuthenticationHolder holder;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioAcessoRepository usuarioAcessoRepository;

    @Test
    @DisplayName("deve trazer o total de animais corretamente por contexto")
    public void deveContar() {
        ContextFactory contextFactory = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository);
        Tenant tenant = contextFactory.umTenantSalvo();
        Tenant tenant2 = contextFactory.umTenantSalvo();
        Animal femea1 = AnimalFactory.umAnimalCompleto(tenant);
        Animal femea2 = AnimalFactory.umAnimalCompleto(tenant);
        AnimalFactory.persistir(animalRepository, femea1);
        AnimalFactory.persistir(animalRepository, femea2);
        AnimalFactory.persistir(animalRepository,  AnimalFactory.umAnimalCompleto(tenant2));

        holder.setTenantId(tenant.getId());
        long deveSer2 = service.total();

        holder.setTenantId(tenant2.getId());
        long deveSer1 = service.total();

        Assertions.assertEquals(2, deveSer2);
        Assertions.assertEquals(1, deveSer1);

        animalRepository.deleteAll();
    }

    @Test
    @DisplayName("deve trazer a quantidade correta de animal")
    public void deveTrazerQuantidadeCorreta() {
        Tenant tenant = new ContextFactory(tenantRepository, tokenService, usuarioAcessoRepository).umTenantSalvo();
        Animal femea1 = AnimalFactory.umAnimalCompleto(tenant);
        Animal femea2 = AnimalFactory.umAnimalCompleto(tenant);
        AnimalFactory.persistir(animalRepository, femea1);
        AnimalFactory.persistir(animalRepository, femea2);

        holder.setTenantId(tenant.getId());
        long totalFemea = service.totalPorSexo(true);
        long totalMacho = service.totalPorSexo(false);

        Assertions.assertEquals(2, totalFemea);
        Assertions.assertEquals(0, totalMacho);

        animalRepository.deleteAll();
    }
}

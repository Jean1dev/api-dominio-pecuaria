package com.binno.dominio.factory;

import com.binno.dominio.module.fazenda.model.Fazenda;
import com.binno.dominio.module.fazenda.repository.FazendaRepository;
import com.binno.dominio.module.tenant.model.Tenant;

public class FazendaFactory {

    public static Fazenda persistir(FazendaRepository repository, Fazenda fazenda) {
        repository.save(fazenda);
        return fazenda;
    }

    public static Fazenda umaFazendaCompleta(Tenant tenant) {
        return Fazenda.builder()
                .nome("Fazenda completa")
                .tenant(tenant)
                .build();
    }
}

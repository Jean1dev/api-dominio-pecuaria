package com.binno.dominio.factory;

import com.binno.dominio.module.fazenda.model.Fazenda;
import com.binno.dominio.module.funcionario.model.Funcionario;
import com.binno.dominio.module.tenant.model.Tenant;

public class FuncionarioFactory {

    public static Funcionario umFuncionarioCompleto(Tenant tenant, Fazenda fazenda) {
        return Funcionario.builder()
                .nome("Juca Rancheiro")
                .tenant(tenant)
                .fazenda(fazenda)
                .build();
    }
}

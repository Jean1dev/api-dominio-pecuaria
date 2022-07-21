package com.binno.dominio.factory;

import com.binno.dominio.module.medicamento.model.Medicamento;
import com.binno.dominio.module.tenant.model.Tenant;

public class MedicamentoFactory {

    public static Medicamento umMedicamento(Tenant tenant) {
        return Medicamento.builder()
                .tenant(tenant)
                .descricao("medicamento")
                .nome("medicamento")
                .build();
    }

    public static Medicamento.MedicamentoBuilder umMedicamentoBuilder(Tenant tenant) {
        return Medicamento.builder()
                .tenant(tenant)
                .descricao("medicamento")
                .nome("medicamento");
    }
}

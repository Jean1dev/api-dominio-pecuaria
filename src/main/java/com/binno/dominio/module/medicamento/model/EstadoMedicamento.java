package com.binno.dominio.module.medicamento.model;

import com.binno.dominio.shared.EnumApplication;

public enum EstadoMedicamento implements EnumApplication {
    DISPONIVEL("Disponivel"), VENCIDO("Vencido");

    private final String description;

    EstadoMedicamento(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}

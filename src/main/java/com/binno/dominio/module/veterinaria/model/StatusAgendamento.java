package com.binno.dominio.module.veterinaria.model;

import com.binno.dominio.shared.EnumApplication;

public enum StatusAgendamento implements EnumApplication {

    PENDENTE("Pendente"), APROVADO("Aprovado"), CONCLUIDO("Concluido");

    private String description;

    StatusAgendamento(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}

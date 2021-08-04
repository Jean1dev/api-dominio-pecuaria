package com.binno.dominio.module.animal.model;

import com.binno.dominio.shared.EnumApplication;

public enum EstadoAtual implements EnumApplication {
    VAZIA("Vazia"), PARIDA("Parida"), PRENHA("Prenha");

    private String description;

    EstadoAtual(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}

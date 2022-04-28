package com.binno.dominio.module.veterinaria.model;

import com.binno.dominio.shared.EnumApplication;

public enum PeriodoDia implements EnumApplication {

    MANHA("manhã"), TARDE("tarde"), NOTURNO("Noturno");

    private String description;

    PeriodoDia(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}

package com.binno.dominio.module.fazenda.model;

import com.binno.dominio.shared.EnumApplication;

public enum TipoMetragem implements EnumApplication {
    HECTARE("Hectare"), ALQUEIRE("Alqueire");

    private String description;

    TipoMetragem(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}

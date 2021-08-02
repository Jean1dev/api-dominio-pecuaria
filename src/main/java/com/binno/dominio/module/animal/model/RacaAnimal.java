package com.binno.dominio.module.animal.model;

import com.binno.dominio.shared.EnumApplication;

public enum RacaAnimal implements EnumApplication<RacaAnimal> {
    NELORE("Nelore"),
    SENEPOL("Senepol"),
    ANGUS("Angus"),
    BRAHMAN("Brahman"),
    BRANGUS("Brangus"),
    HEREFORD("Hereford"),
    CARACU("Caracu"),
    CHAROLES("Charoles"),
    GUZERA("Guzera"),
    TABAPUA("Tabupua"),
    BUFALO("Bufalo");

    private String description;

    RacaAnimal(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}

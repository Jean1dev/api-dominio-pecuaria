package com.binno.dominio.shared;

public interface EnumApplication<T extends Enum<T>>{

    String name();

    String getDescription();
}

package com.binno.dominio.shared.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KeyAndValueDto implements Serializable {

    private String chave;

    private String valor;
}

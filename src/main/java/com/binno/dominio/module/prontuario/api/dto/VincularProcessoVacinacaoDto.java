package com.binno.dominio.module.prontuario.api.dto;

import com.binno.dominio.module.animal.model.Animal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class VincularProcessoVacinacaoDto {
    private final Integer processoVacinacaoId;
    private final Animal animal;
}

package com.binno.dominio.module.prontuario.api.dto;

import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.prontuario.model.Prontuario;
import com.binno.dominio.module.vacinacao.model.ProcessoVacinacao;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public final class DadosProntuarioDto {
    private final Animal animal;
    private final Prontuario prontuario;
    private final List<ProcessoVacinacao> processoVacinacaoList;
}

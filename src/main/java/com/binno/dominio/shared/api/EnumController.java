package com.binno.dominio.shared.api;

import com.binno.dominio.module.animal.model.EstadoAtual;
import com.binno.dominio.module.animal.model.RacaAnimal;
import com.binno.dominio.module.fazenda.model.TipoMetragem;
import com.binno.dominio.module.veterinaria.model.PeriodoDia;
import com.binno.dominio.module.veterinaria.model.StatusAgendamento;
import com.binno.dominio.shared.EnumApplication;
import com.binno.dominio.shared.api.dto.KeyAndValueDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(EnumController.PATH)
public class EnumController {

    public static final String PATH = "enums";

    @GetMapping
    @ApiOperation("Retorna os Enums da aplicacao")
    public Map getEnums() {
        return Map.of(
                EstadoAtual.class.getSimpleName(), criarListaDoEnum(EstadoAtual.values()),
                RacaAnimal.class.getSimpleName(), criarListaDoEnum(RacaAnimal.values()),
                TipoMetragem.class.getSimpleName(), criarListaDoEnum(TipoMetragem.values()),
                PeriodoDia.class.getSimpleName(), criarListaDoEnum(PeriodoDia.values()),
                StatusAgendamento.class.getSimpleName(), criarListaDoEnum(StatusAgendamento.values())
        );
    }

    private List<KeyAndValueDto> criarListaDoEnum(EnumApplication[] values) {
        return Arrays.stream(values).map(enumValue -> KeyAndValueDto.builder()
                        .chave(enumValue.name())
                        .valor(enumValue.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}

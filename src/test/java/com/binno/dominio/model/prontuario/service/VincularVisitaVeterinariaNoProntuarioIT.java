package com.binno.dominio.model.prontuario.service;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.module.prontuario.model.Prontuario;
import com.binno.dominio.module.prontuario.repository.ProntuarioRepository;
import com.binno.dominio.module.prontuario.service.VincularVisitaVeterinariaNoProntuario;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.veterinaria.model.AgendamentoVeterinario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@DisplayName("VincularVisitaVeterinariaNoProntuario Test")
public class VincularVisitaVeterinariaNoProntuarioIT extends ApplicationConfigIT {

    @Autowired
    private VincularVisitaVeterinariaNoProntuario service;

    @MockBean
    private ProntuarioRepository repository;

    @DisplayName("deve vincular agendamento no prontuario")
    @Test
    public void deveVincular() {
        AgendamentoVeterinario agendamentoVeterinario = AgendamentoVeterinario.builder()
                .tenant(Tenant.of(1))
                .id(1)
                .build();

        Prontuario prontuario = mock(Prontuario.class);
        when(prontuario.getParecerVeterinarioSeparadoPorVirgula()).thenReturn(null);

        when(repository.findAllByTenantId(anyInt())).thenReturn(Collections.singletonList(prontuario));

        service.executar(agendamentoVeterinario);

        verify(repository, times(1)).saveAll(anyIterable());
    }
}

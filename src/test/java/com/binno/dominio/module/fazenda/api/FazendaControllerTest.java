package com.binno.dominio.module.fazenda.api;

import com.binno.dominio.module.fazenda.api.dto.FazendaDto;
import com.binno.dominio.module.fazenda.repository.FazendaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FazendaControllerTest {

    @MockBean
    FazendaRepository repository;

//    @Test
//    void deveCriarFazenda() {
//        FazendaDto dto = FazendaDto.builder()
//                .nome("Alceu")
//                .endereco("Rua dos Albinos")
//                .capacidadeMaxGado(500)
//                .codigoEstab(123456)
//                .tamanhoHectare(100)
//                .build();
//
//        assertEquals(dto.getNome(), "Alceu");
//        assertEquals(dto.getEndereco(), "Rua dos Albinos");
//        assertEquals(dto.getCapacidadeMaxGado(), 500);
//        assertEquals(dto.getCodigoEstab(), 123456);
//        assertEquals(dto.getTamanhoHectare(), 123456);
//    }


}
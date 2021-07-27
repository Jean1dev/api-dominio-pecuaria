package com.binno.dominio.module.animal.service;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.api.dto.AnimalComPesoDto;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.model.PesoAnimal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.animal.repository.PesoAnimalRepository;
import com.binno.dominio.module.tenant.model.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Objects;

@Service
@Transactional
public class CriarAnimalService {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private AuthenticationHolder holder;

    @Autowired
    private PesoAnimalRepository pesoAnimalRepository;

    public Animal executar(AnimalComPesoDto animalComPesoDto) {
        PesoAnimal pesoAnimal = null;
        if (!Objects.isNull(animalComPesoDto.getPeso())) {
            pesoAnimal = pesoAnimalRepository.save(PesoAnimal.builder()
                    .dataPesagem(animalComPesoDto.getDataPesagem())
                    .peso(animalComPesoDto.getPeso())
                    .idadeEmDias(animalComPesoDto.getIdadeEmDias())
                    .build());
        }

        return animalRepository.save(Animal.builder()
                .numero(animalComPesoDto.getNumero())
                .raca(animalComPesoDto.getRaca())
                .apelido(animalComPesoDto.getApelido())
                .dataNascimento(animalComPesoDto.getDataNascimento())
                .numeroCrias(animalComPesoDto.getNumeroCrias())
                .estadoAtual(animalComPesoDto.getEstadoAtual())
                .dataUltimoParto(animalComPesoDto.getDataUltimoParto())
                .descarteFuturo(animalComPesoDto.getDescarteFuturo())
                .isFemea(animalComPesoDto.getIsFemea())
                .justificativaDescarteFuturo(animalComPesoDto.getJustificativaDescarteFuturo())
                .fazenda(animalComPesoDto.getFazenda())
                .tenant(Tenant.of(holder.getTenantId()))
                .pesoAnimal(Collections.singletonList(pesoAnimal))
                .build());

    }
}

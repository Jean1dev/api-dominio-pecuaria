package com.binno.dominio.module.animal.service;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.api.dto.CriarAnimalDto;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.model.PesoAnimal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.animal.repository.PesoAnimalRepository;
import com.binno.dominio.module.imagem.model.Imagem;
import com.binno.dominio.module.imagem.repository.ImagemRepository;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.shared.RegraNegocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class CriarAnimalService implements RegraNegocioService<Animal, CriarAnimalDto> {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private AuthenticationHolder holder;

    @Autowired
    private PesoAnimalRepository pesoAnimalRepository;

    @Autowired
    private ImagemRepository imagemRepository;

    @Override
    public Animal executar(CriarAnimalDto criarAnimalDto) {
        Animal animal = animalRepository.save(Animal.builder()
                .id(criarAnimalDto.getId())
                .numero(criarAnimalDto.getNumero())
                .raca(criarAnimalDto.getRaca())
                .apelido(criarAnimalDto.getApelido())
                .dataNascimento(criarAnimalDto.getDataNascimento())
                .numeroCrias(criarAnimalDto.getNumeroCrias())
                .estadoAtual(criarAnimalDto.getEstadoAtual())
                .dataUltimoParto(criarAnimalDto.getDataUltimoParto())
                .descarteFuturo(criarAnimalDto.getDescarteFuturo())
                .isFemea(criarAnimalDto.getIsFemea())
                .justificativaDescarteFuturo(criarAnimalDto.getJustificativaDescarteFuturo())
                .fazenda(criarAnimalDto.getFazenda())
                .tenant(Tenant.of(holder.getTenantId()))
                .build());

        registrarPesoAnimal(animal, criarAnimalDto);
        registrarImagens(animal, criarAnimalDto);
        return animal;
    }

    private void registrarImagens(Animal animal, CriarAnimalDto criarAnimalDto) {
        if (Objects.isNull(criarAnimalDto.getImagens()))
            return;

        criarAnimalDto
                .getImagens()
                .forEach(img -> {
                    imagemRepository.save(Imagem.builder()
                            .id(img.getId())
                            .url(img.getImagemUrl())
                            .referenciaAnimal(animal)
                            .build());
                });
    }

    private void registrarPesoAnimal(Animal animal, CriarAnimalDto criarAnimalDto) {
        if (Objects.isNull(criarAnimalDto.getPesos()))
            return;

        criarAnimalDto
                .getPesos()
                .forEach(peso -> {
                    pesoAnimalRepository.save(PesoAnimal.builder()
                            .id(peso.getId())
                            .dataPesagem(peso.getDataPesagem())
                            .peso(peso.getPeso())
                            .animal(animal)
                            .build());
                });
    }
}

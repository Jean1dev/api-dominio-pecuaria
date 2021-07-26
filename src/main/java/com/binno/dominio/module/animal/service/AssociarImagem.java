package com.binno.dominio.module.animal.service;

import com.binno.dominio.module.animal.api.dto.AssociarImagemNoAnimalDto;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.imagem.model.Imagem;
import com.binno.dominio.module.imagem.repository.ImagemRepository;
import com.binno.dominio.shared.RegraNegocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AssociarImagem implements RegraNegocioService<Animal, AssociarImagemNoAnimalDto> {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private ImagemRepository imagemRepository;

    @Override
    public Animal executar(AssociarImagemNoAnimalDto dto) {
        Animal animal = animalRepository.findById(dto.getAnimalId()).orElseThrow();
        Imagem imagem = imagemRepository.save(Imagem.builder()
                .url(dto.getImagemUrl())
                .referenciaAnimal(animal)
                .build());

        return animal;
    }
}

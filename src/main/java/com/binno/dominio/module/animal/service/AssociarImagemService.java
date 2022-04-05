package com.binno.dominio.module.animal.service;

import com.binno.dominio.module.animal.api.dto.ImagemAnimalDto;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.imagem.model.Imagem;
import com.binno.dominio.module.imagem.repository.ImagemRepository;
import com.binno.dominio.shared.RegraNegocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AssociarImagemService implements RegraNegocioService<Animal, ImagemAnimalDto> {

    private final AnimalRepository animalRepository;

    private final ImagemRepository imagemRepository;

    @Override
    public Animal executar(ImagemAnimalDto dto) {
        Animal animal = animalRepository.findById(dto.getAnimalId()).orElseThrow();
        imagemRepository.save(Imagem.builder()
                .url(dto.getImagemUrl())
                .referenciaAnimal(animal)
                .build());

        return animal;
    }
}

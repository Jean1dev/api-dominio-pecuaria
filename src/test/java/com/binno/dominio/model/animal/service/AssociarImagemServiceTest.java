package com.binno.dominio.model.animal.service;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.module.animal.api.dto.ImagemAnimalDto;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.animal.service.AssociarImagemService;
import com.binno.dominio.module.imagem.repository.ImagemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@DisplayName("AssociarImagemService")
public class AssociarImagemServiceTest extends ApplicationConfigIT {

    @Autowired
    private AssociarImagemService service;

    @MockBean
    private AnimalRepository animalRepository;

    @MockBean
    private ImagemRepository imagemRepository;

    @Test
    @DisplayName("deve associar uma imagem no animal")
    public void deveAssociarImagemEmAnimal() {
        ImagemAnimalDto animalDto = ImagemAnimalDto.builder()
                .animalId(1)
                .imagemUrl("http://localhost:3000/img")
                .build();

        Animal animalMOck = Mockito.mock(Animal.class);
        Mockito.when(animalMOck.getApelido()).thenReturn("Vaquinha Mimosa");

        Optional<Animal> optionalAnimal = Optional.of(animalMOck);
        Mockito.when(animalRepository.findById(ArgumentMatchers.anyInt())).thenReturn(optionalAnimal);

        Animal animal = service.executar(animalDto);

        Assertions.assertEquals(animal.getApelido(), animalMOck.getApelido());
    }

    @Test
    @DisplayName("deve lançar exception porque o animal nao existe")
    public void deveLancarException() {
        ImagemAnimalDto animalDto = ImagemAnimalDto.builder()
                .animalId(1)
                .build();

        Mockito.when(animalRepository.findById(ArgumentMatchers.anyInt())).thenThrow(RuntimeException.class);

        Assertions.assertThrows(RuntimeException.class, () -> service.executar(animalDto));
    }
}

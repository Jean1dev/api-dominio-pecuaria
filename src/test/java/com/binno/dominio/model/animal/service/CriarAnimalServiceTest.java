package com.binno.dominio.model.animal.service;

import com.binno.dominio.ApplicationConfigIT;
import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.api.dto.CriarAnimalDto;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.model.EstadoAtual;
import com.binno.dominio.module.animal.model.PesoAnimal;
import com.binno.dominio.module.animal.model.RacaAnimal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.animal.repository.PesoAnimalRepository;
import com.binno.dominio.module.animal.service.CriarAnimalService;
import com.binno.dominio.module.imagem.model.Imagem;
import com.binno.dominio.module.imagem.repository.ImagemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CriarAnimalService")
public class CriarAnimalServiceTest extends ApplicationConfigIT {

    @Autowired
    private CriarAnimalService service;

    @MockBean
    private AuthenticationHolder holder;

    @MockBean
    private PesoAnimalRepository pesoAnimalRepository;

    @MockBean
    private ImagemRepository imagemRepository;

    @MockBean
    private AnimalRepository animalRepository;

    @Test
    @DisplayName("deve criar uma animal sem peso e sem imagens")
    public void deveCriarAnimal() {
        CriarAnimalDto dto = CriarAnimalDto.builder()
                .apelido("Apelido")
                .dataNascimento(LocalDate.now())
                .numero(1)
                .raca(RacaAnimal.NELORE)
                .descarteFuturo(false)
                .estadoAtual(EstadoAtual.VAZIA)
                .build();

        Animal animal = mock(Animal.class);
        when(animalRepository.save(any(Animal.class))).thenReturn(animal);

        service.executar(dto);

        verify(pesoAnimalRepository, never()).save(any(PesoAnimal.class));
        verify(imagemRepository, never()).save(any(Imagem.class));
        verify(animalRepository, times(1)).save(any(Animal.class));
    }

    @Test
    @DisplayName("deve criar um animal com peso e imagens")
    public void deveCriarAnimalCompleto() {
        CriarAnimalDto dto = CriarAnimalDto.builder()
                .apelido("Apelido")
                .dataNascimento(LocalDate.now())
                .numero(1)
                .raca(RacaAnimal.NELORE)
                .descarteFuturo(false)
                .estadoAtual(EstadoAtual.VAZIA)
                .peso(80)
                .dataPesagem(LocalDate.now())
                .idadeEmDias(25)
                .imagens(Set.of("url1", "url2"))
                .build();

        Animal animal = mock(Animal.class);
        when(animalRepository.save(any(Animal.class))).thenReturn(animal);

        service.executar(dto);

        verify(pesoAnimalRepository, times(1)).save(any(PesoAnimal.class));
        verify(imagemRepository, times(2)).save(any(Imagem.class));
        verify(animalRepository, times(1)).save(any(Animal.class));
    }
}

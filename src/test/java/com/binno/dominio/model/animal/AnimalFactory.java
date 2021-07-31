package com.binno.dominio.model.animal;

import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.model.EstadoAtual;
import com.binno.dominio.module.animal.model.PesoAnimal;
import com.binno.dominio.module.animal.model.RacaAnimal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.imagem.model.Imagem;
import com.binno.dominio.module.tenant.model.Tenant;

import java.time.LocalDate;

public class AnimalFactory {

    public static Animal persistir(AnimalRepository repository, Animal animal) {
        repository.save(animal);
        return animal;
    }

    public static Animal umAnimalCompleto(Tenant tenant) {
        return Animal.builder()
                .numero(1)
                .raca(RacaAnimal.NELORE)
                .apelido("BOI GORDO")
                .numeroCrias(0)
                .estadoAtual(EstadoAtual.VAZIA)
                .dataNascimento(LocalDate.now())
                .dataUltimoParto(LocalDate.now())
                .descarteFuturo(false)
                .justificativaDescarteFuturo("nao tem")
                .isFemea(true)
                .tenant(tenant)
                .build();
    }

    public static PesoAnimal umPesoAnimal(Animal animal) {
        return PesoAnimal.builder()
                .animal(animal)
                .peso(85.0)
                .dataPesagem(LocalDate.now())
                .idadeEmDias(25)
                .build();
    }

    public static Imagem umaImagem(Animal animal) {
        return Imagem.builder()
                .referenciaAnimal(animal)
                .url("url")
                .build();
    }
}

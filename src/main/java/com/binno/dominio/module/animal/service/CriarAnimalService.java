package com.binno.dominio.module.animal.service;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.animal.api.dto.AnimalDto;
import com.binno.dominio.module.animal.api.dto.CriarAnimalDto;
import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.model.PesoAnimal;
import com.binno.dominio.module.animal.repository.AnimalRepository;
import com.binno.dominio.module.animal.repository.PesoAnimalRepository;
import com.binno.dominio.module.fazenda.model.Fazenda;
import com.binno.dominio.module.fazenda.repository.FazendaRepository;
import com.binno.dominio.module.imagem.model.Imagem;
import com.binno.dominio.module.imagem.repository.ImagemRepository;
import com.binno.dominio.module.notificacao.service.RegistrarNotificacao;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.shared.RegraNegocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CriarAnimalService implements RegraNegocioService<Animal, CriarAnimalDto> {

    private final AnimalRepository animalRepository;

    private final AuthenticationHolder holder;

    private final PesoAnimalRepository pesoAnimalRepository;

    private final ImagemRepository imagemRepository;

    private final RegistrarNotificacao registrarNotificacao;

    private final FazendaRepository fazendaRepository;

    @Override
    public Animal executar(CriarAnimalDto criarAnimalDto) {
        Animal animal = animalRepository.save(AnimalDto.DtoToAnimal(criarAnimalDto, holder));

        registrarPesoAnimal(animal, criarAnimalDto);
        registrarImagens(animal, criarAnimalDto);
        verificarCapacidadeFazenda(animal.getFazenda());
        return animal;
    }

    private void verificarCapacidadeFazenda(Fazenda fazenda) {
        if (Objects.isNull(fazenda.getNome())) {
            fazenda = fazendaRepository.findById(fazenda.getId()).orElseThrow();
        }

        if (Objects.isNull(fazenda.getCapMaximaGado())) {
            registrarNotificacao.executar("A fazenda " + fazenda.getNome() + " não tem a capacidade maxima de gado preenchida");
            return;
        }

        //TODO: agendar um serviço q faça isso de forma async
        long totalAnimaisNaFAzenda = animalRepository.countAllByTenantIdAndFazendaId(holder.getTenantId(), fazenda.getId());
        Integer fazendaCapMaximaGado = fazenda.getCapMaximaGado();

        if (totalAnimaisNaFAzenda > fazendaCapMaximaGado) {
            registrarNotificacao.executar("A fazenda " + fazenda.getNome() + " ja atingiu a capacidade maxima de gado. Quantidade atual:" + totalAnimaisNaFAzenda);
        }
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

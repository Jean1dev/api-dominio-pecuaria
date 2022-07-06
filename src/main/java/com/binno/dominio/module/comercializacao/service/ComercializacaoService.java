package com.binno.dominio.module.comercializacao.service;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.comercializacao.api.dto.AnimalParaComercializacaoDto;
import com.binno.dominio.module.comercializacao.http.IComercializacaoExternalApis;
import com.binno.dominio.module.comercializacao.http.dto.CambioDto;
import com.binno.dominio.module.comercializacao.http.dto.EnviarParaComercializacaoDto;
import com.binno.dominio.module.tenant.model.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ComercializacaoService {

    private final IComercializacaoExternalApis externalApis;

    private HashMap<Tenant, List<AnimalParaComercializacaoDto>> comercializacaoDtoHashMap = new HashMap<>();

    private final AuthenticationHolder holder;

    public CambioDto getUltimaCotacao() {
        return externalApis.getListCambio().stream().findFirst().orElseThrow();
    }

    public void enviarParaComercializacao() {
        List<EnviarParaComercializacaoDto> collect = recuperarListaDoUsuario()
                .stream()
                .map(dto -> EnviarParaComercializacaoDto.builder()
                        .animalReferencia(dto.getAnimalReferencia())
                        .sequencial(dto.getSequencial())
                        .tenantId(holder.getTenantId())
                        .valorCambio(dto.getValorCambio())
                        .valorSugerido(dto.getValorSugerido())
                        .build())
                .collect(Collectors.toList());

        externalApis.enviarParaComercializacao(collect);
    }

    public void adicionarAnimalNaComercializacao(AnimalParaComercializacaoDto dto) {
        if (naoDeveAdicionarAnimalDuplicado(dto))
            return;

        if (Objects.isNull(dto.getValorSugerido()))
            dto.setValorSugerido((double) 0);

        if (Objects.isNull(dto.getValorCambio()))
            dto.setValorCambio(getUltimaCotacao().getValor());

        if (Objects.isNull(dto.getSequencial()))
            dto.setSequencial(descobreAndRetornaSequencial());

        List<AnimalParaComercializacaoDto> listaDoUsuario = recuperarListaDoUsuario();
        if (listaDoUsuario.isEmpty()) {
            List<AnimalParaComercializacaoDto> muttableList = new ArrayList<>();
            muttableList.add(dto);
            comercializacaoDtoHashMap.put(Tenant.of(holder.getTenantId()), muttableList);
        } else {
            listaDoUsuario.add(dto);
        }
    }

    public void removerAnimalNaComercializacao(AnimalParaComercializacaoDto dto) {
        recuperarListaDoUsuario()
                .removeIf(animalRef -> animalRef.getAnimalReferencia().equals(dto.getAnimalReferencia()));
    }

    private boolean naoDeveAdicionarAnimalDuplicado(AnimalParaComercializacaoDto dto) {
        return recuperarListaDoUsuario()
                .stream()
                .anyMatch(animalRef -> animalRef.getAnimalReferencia().equals(dto.getAnimalReferencia()));
    }

    private Integer descobreAndRetornaSequencial() {
        List<AnimalParaComercializacaoDto> listDoUsuario = recuperarListaDoUsuario();
        if (listDoUsuario.isEmpty())
            return 1;

        List<Integer> sequencialList = listDoUsuario
                .stream()
                .map(AnimalParaComercializacaoDto::getSequencial)
                .sorted()
                .collect(Collectors.toUnmodifiableList());

        return sequencialList.get(sequencialList.size() - 1) + 1;
    }

    public List<AnimalParaComercializacaoDto> recuperarListaDoUsuario() {
        Tenant _tenant = comercializacaoDtoHashMap
                .keySet()
                .stream()
                .filter(tenant -> tenant.getId().equals(holder.getTenantId()))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(_tenant))
            return Collections.emptyList();

        return comercializacaoDtoHashMap.get(_tenant);
    }
}

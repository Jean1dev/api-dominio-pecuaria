package com.binno.dominio.module.comercializacao.http;

import com.binno.dominio.module.comercializacao.http.dto.CambioDto;
import com.binno.dominio.module.comercializacao.http.dto.EnviarParaComercializacaoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ComercializacaoExternalApis implements IComercializacaoExternalApis {

    private final RestTemplate restTemplate;

    @Override
    public List<CambioDto> getListCambio() {
        String uri = UriComponentsBuilder.fromHttpUrl("https://comercializacao.herokuapp.com")
                .pathSegment("v1", "cambio")
                .build()
                .toUriString();

        HttpEntity<Object> httpEntity = new HttpEntity<>(null, null);
        ResponseEntity<CambioDto[]> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, CambioDto[].class);
        return List.of(Objects.requireNonNull(responseEntity.getBody()));
    }

    @Override
    public void enviarParaComercializacao(List<EnviarParaComercializacaoDto> items) {
        String uri = UriComponentsBuilder.fromHttpUrl("https://leilao-app.herokuapp.com")
                .pathSegment("v1", "comercializacao")
                .build()
                .toUriString();

        HttpEntity<List<EnviarParaComercializacaoDto>> httpEntity = new HttpEntity<>(items, null);
        HttpStatus statusCode = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, Object.class).getStatusCode();
        if (HttpStatus.OK != statusCode) {
            throw new HttpClientErrorException(statusCode);
        }
    }
}

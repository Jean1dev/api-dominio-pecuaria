package com.binno.dominio.module.comercializacao.http;

import com.binno.dominio.module.comercializacao.http.dto.CambioDto;
import com.binno.dominio.module.comercializacao.http.dto.EnviarParaComercializacaoDto;

import java.util.List;

public interface IComercializacaoExternalApis {

    List<CambioDto> getListCambio();

    void enviarParaComercializacao(List<EnviarParaComercializacaoDto> items);
}

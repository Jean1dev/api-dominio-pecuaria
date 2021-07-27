package com.binno.dominio.shared;

/**
 * @T -> Representa a classe de Dominio
 * @S -> representa a classe DTO
 */
public interface RegraNegocioService<T, S> {

    T executar(S dto);
}

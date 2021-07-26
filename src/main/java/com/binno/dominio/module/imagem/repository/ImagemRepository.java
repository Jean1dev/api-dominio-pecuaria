package com.binno.dominio.module.imagem.repository;

import com.binno.dominio.module.imagem.model.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagemRepository extends JpaRepository<Imagem, Integer> {
}

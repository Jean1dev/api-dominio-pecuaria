package com.binno.dominio.module.veterinaria.repository;

import com.binno.dominio.module.veterinaria.model.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeterinarioRepository extends JpaRepository<Veterinario, Integer> {
}

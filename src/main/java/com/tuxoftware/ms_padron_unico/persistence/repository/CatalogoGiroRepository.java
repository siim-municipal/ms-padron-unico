package com.tuxoftware.ms_padron_unico.persistence.repository;

import com.tuxoftware.ms_padron_unico.persistence.entity.CatalogoGiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogoGiroRepository extends JpaRepository<CatalogoGiro, String> {
    // Para llenar el dropdown del frontend
    List<CatalogoGiro> findByActivoTrueOrderByDescripcionAsc();
}

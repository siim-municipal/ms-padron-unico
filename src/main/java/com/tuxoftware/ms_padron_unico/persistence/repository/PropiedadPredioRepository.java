package com.tuxoftware.ms_padron_unico.persistence.repository;

import com.tuxoftware.ms_padron_unico.persistence.entity.PropiedadPredio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PropiedadPredioRepository extends JpaRepository<PropiedadPredio, UUID> {
}

package com.tuxoftware.ms_padron_unico.persistence.repository;

import com.tuxoftware.ms_padron_unico.persistence.entity.LicenciaComercial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LicenciaComercialRepository extends JpaRepository<LicenciaComercial, UUID> {

    boolean existsByNumeroLicencia(String numeroLicencia);

    // Búsqueda por placa (Requirement específico)
    Optional<LicenciaComercial> findByNumeroPlaca(String numeroPlaca);

    // Búsqueda por número de licencia
    Optional<LicenciaComercial> findByNumeroLicencia(String numeroLicencia);
}

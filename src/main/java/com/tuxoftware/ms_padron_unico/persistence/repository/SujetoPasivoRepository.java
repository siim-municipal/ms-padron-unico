package com.tuxoftware.ms_padron_unico.persistence.repository;

import com.tuxoftware.ms_padron_unico.persistence.entity.SujetoPasivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SujetoPasivoRepository extends JpaRepository<SujetoPasivo, UUID>, JpaSpecificationExecutor<SujetoPasivo> {
    boolean existsByRfc(String rfc);
}
package com.tuxoftware.ms_padron_unico.persistence.repository;

import com.tuxoftware.ms_padron_unico.dto.PropietarioDTO;
import com.tuxoftware.ms_padron_unico.persistence.entity.PropiedadPredio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PropiedadPredioRepository extends JpaRepository<PropiedadPredio, UUID> {
    // Query optimizada: Busca los responsables de pago de una lista de IDs de predios
    // Usamos JOIN FETCH para traer el SujetoPasivo de un solo golpe
    @Query("SELECT pp FROM PropiedadPredio pp " +
            "JOIN FETCH pp.sujeto " +
            "WHERE pp.predio.id IN :predioIds AND pp.esResponsablePago = true")
    List<PropiedadPredio> findResponsablesPorPredioIds(@Param("predioIds") List<UUID> predioIds);

    @Query("SELECT new com.tuxoftware.ms_padron_unico.dto.PropietarioDTO(" +
            "p.sujeto.id, p.sujeto.nombreRazonSocial, " +
            "p.sujeto.apellidoPaterno, p.sujeto.apellidoMaterno, p.sujeto.rfc, p.esResponsablePago) " +
            "FROM PropiedadPredio p WHERE p.predio.id = :predioId")
    List<PropietarioDTO> findResumenByPredioId(@Param("predioId") UUID predioId);
}

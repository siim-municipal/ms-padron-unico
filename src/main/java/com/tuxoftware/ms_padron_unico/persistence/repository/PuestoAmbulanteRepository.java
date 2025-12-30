package com.tuxoftware.ms_padron_unico.persistence.repository;

import com.tuxoftware.ms_padron_unico.persistence.entity.PuestoAmbulante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PuestoAmbulanteRepository extends JpaRepository<PuestoAmbulante, UUID> {

    // Encuentra puestos ambulantes en un radio de X metros
    // ST_DWithin usa SRID 4326 (grados), así que para metros usamos::geography
    // O usamos la proyección nativa de PostGIS
    @Query(value = "SELECT * FROM puestos_ambulantes p " +
            "WHERE ST_DWithin(p.ubicacion_actual::geography, :punto::geography, :radioMetros) " +
            "AND p.estatus = 'ACTIVO'",
            nativeQuery = true)
    List<PuestoAmbulante> encontrarCercanos(
            @Param("punto") Point punto,
            @Param("radioMetros") double radioMetros);
}
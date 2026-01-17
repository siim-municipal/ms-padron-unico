package com.tuxoftware.ms_padron_unico.persistence.repository;

import com.tuxoftware.ms_padron_unico.persistence.entity.Predio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PredioRepository extends JpaRepository<Predio, UUID>, JpaSpecificationExecutor<Predio> {

    Optional<Predio> findByClaveCatastral(String claveCatastral);

    // Consulta Espacial: Encuentra predios que contengan un punto específico
    // (Ej.: Hago clic en el mapa y quiero saber qué predio es)
    @Query(value = "SELECT p FROM Predio p WHERE contains(p.poligonoLimite, :punto) = true")
    Optional<Predio> buscarPredioPorCoordenada(@Param("punto") Point punto);

    // Búsqueda por colonia
    List<Predio> findByColoniaBarrio(String colonia);

    boolean existsByClaveCatastral(String claveCatastral);

    // Busca predios dentro de un radio de 'metros' desde un punto (lon, lat)
    @Query(value = """
        SELECT * FROM tuxtepec.predios 
        WHERE ubicacion_centro IS NOT NULL 
        AND ST_DWithin(
            ubicacion_centro::geography, 
            ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography, 
            :metros
        )
        """, nativeQuery = true)
    List<Predio> buscarCercanos(
            @Param("lon") double longitud,
            @Param("lat") double latitud,
            @Param("metros") double metros
    );
}
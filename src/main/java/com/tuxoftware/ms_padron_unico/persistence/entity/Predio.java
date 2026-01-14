package com.tuxoftware.ms_padron_unico.persistence.entity;

import com.tuxoftware.ms_padron_unico.enums.TipoPredio;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.MultiPolygon;
import java.math.BigDecimal;

@Entity
@Table(name = "predios")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Predio extends BaseEntity {

    @Column(name = "clave_catastral", nullable = false, unique = true)
    private String claveCatastral;

    @Column(name = "clave_anterior")
    private String claveAnterior;

    @Column(name = "cuenta_predial")
    private String cuentaPredial;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_predio", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TipoPredio tipoPredio;

    @Column(name = "uso_suelo")
    private String usoSuelo;

    @Column(name = "valor_catastral", precision = 18, scale = 2)
    private BigDecimal valorCatastral;

    @Column(name = "area_terreno_m2", precision = 12, scale = 4)
    private BigDecimal areaTerrenoM2;

    @Column(name = "area_construccion_m2", precision = 12, scale = 4)
    private BigDecimal areaConstruccionM2;

    // GEOESPACIAL (POSTGIS)

    @Column(name = "ubicacion_centro", columnDefinition = "geometry(Point,4326)")
    private Point ubicacionCentro;

    @Column(name = "poligono_limite", columnDefinition = "geometry(MultiPolygon,4326)")
    private MultiPolygon poligonoLimite;

    // DIRECCIÓN
    private String calle;

    @Column(name = "numero_exterior")
    private String numeroExterior;

    @Column(name = "numero_interior")
    private String numeroInterior;

    @Column(name = "colonia_barrio")
    private String coloniaBarrio;

    @Column(name = "codigo_postal")
    private String codigoPostal;

    @Column(name = "ultimo_anio_pagado")
    private Integer ultimoAnioPagado;

}
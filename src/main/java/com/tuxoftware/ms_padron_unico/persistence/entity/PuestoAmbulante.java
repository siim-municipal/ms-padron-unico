package com.tuxoftware.ms_padron_unico.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "puestos_ambulantes")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PuestoAmbulante extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sujeto_id", nullable = false)
    private SujetoPasivo sujeto;

    @Column(name = "giro_comercial")
    private String giroComercial;

    @Column(name = "dimensiones_m2", precision = 10, scale = 2)
    private BigDecimal dimensionesM2;

    @Column(name = "dias_operacion")
    private String diasOperacion; // Ej: "LMXJVSD"

    @Column(name = "ubicacion_actual", columnDefinition = "geometry(Point,4326)")
    private Point ubicacionActual;

    @Column(name = "referencia_ubicacion", columnDefinition = "TEXT")
    private String referenciaUbicacion;

    @Column(name = "ultimo_pago_fecha")
    private LocalDate ultimoPagoFecha;
}
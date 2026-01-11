package com.tuxoftware.ms_padron_unico.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "licencias_comerciales")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class LicenciaComercial extends BaseEntity {

    // Identificador único "humano" (Ej: LIC-2025-001)
    @Column(name = "numero_licencia", unique = true, nullable = false)
    private String numeroLicencia;

    // Relación con el Dueño del Negocio (Sujeto Pasivo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sujeto_id", nullable = false)
    private SujetoPasivo sujeto;

    // Relación con la Ubicación Física (Predio)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predio_id", nullable = false)
    private Predio predio;

    // Nombre del establecimiento (Ej: "Abarrotes La Esquinita")
    @Column(name = "nombre_comercial", nullable = false)
    private String nombreComercial;

    // Clave del giro para conectar con las Tarifas (Ej: "MINISUPER_CON_ALCOHOL")
    // Esto es lo que ms-calculo usará para saber cuánto cobrar
    @Column(name = "giro_clave", nullable = false)
    private String giroClave;

    // Descripción detallada del giro si es necesario
    @Column(name = "giro_descripcion")
    private String giroDescripcion;

    // Superficie ocupada por el negocio (Vital para Art. 105)
    @Column(name = "metros_cuadrados", precision = 10, scale = 2)
    private BigDecimal metrosCuadrados;

    // Horario autorizado (Importante para cobro de horas extra)
    @Column(name = "horario_funcionamiento")
    private String horarioFuncionamiento;

    // Fechas de control
    @Column(name = "fecha_apertura")
    private LocalDate fechaApertura;

    @Column(name = "fecha_ultima_renovacion")
    private LocalDate fechaUltimaRenovacion;

    @Column(name = "anio_fiscal_cubierto")
    private Integer anioFiscalCubierto;

    // Estatus específico de la licencia (independiente del estatus de borrado lógico)
    // Ej: ACTIVA, SUSPENDIDA, CLAUSURADA, EN_TRAMITE
    @Column(name = "estado_licencia")
    private String estadoLicencia;

    // Placa de funcionamiento (si se emite física)
    @Column(name = "numero_placa")
    private String numeroPlaca;
}

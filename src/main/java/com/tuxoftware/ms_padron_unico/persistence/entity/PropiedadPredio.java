package com.tuxoftware.ms_padron_unico.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "propiedad_predios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropiedadPredio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sujeto_id", nullable = false)
    private SujetoPasivo sujeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predio_id", nullable = false)
    private Predio predio;

    @Column(name = "tipo_relacion")
    private String tipoRelacion; // PROPIETARIO, POSEEDOR

    @Column(name = "porcentaje_propiedad", precision = 5, scale = 2)
    private BigDecimal porcentajePropiedad;

    @Column(name = "es_responsable_pago")
    private Boolean esResponsablePago;

    @Column(name = "fecha_escrituracion")
    private LocalDate fechaEscrituracion;

    @Column(name = "numero_escritura")
    private String numeroEscritura;
}
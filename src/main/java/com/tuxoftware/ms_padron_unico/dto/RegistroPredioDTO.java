package com.tuxoftware.ms_padron_unico.dto;

import com.tuxoftware.ms_padron_unico.enums.TipoPredio;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class RegistroPredioDTO {
    // DATOS DEL PREDIO
    @NotBlank(message = "La clave catastral es obligatoria")
    private String claveCatastral;

    private String claveAnterior;
    private String cuentaPredial;

    @NotNull(message = "El tipo de predio es obligatorio")
    private TipoPredio tipoPredio;

    private String usoSuelo;

    @Positive
    private BigDecimal valorCatastral;

    private BigDecimal areaTerrenoM2;
    private BigDecimal areaConstruccionM2;

    // DIRECCIÓN
    private String calle;
    private String numeroExterior;
    private String numeroInterior;
    private String coloniaBarrio;
    private String codigoPostal;

    // DATOS DE LA RELACIÓN (PROPIETARIO INICIAL)
    @NotNull(message = "El ID del propietario es obligatorio")
    private UUID sujetoId;

    @DecimalMin("0.01")
    @DecimalMax("100.00")
    private BigDecimal porcentajePropiedad; // Ej.: 100.00 si es único dueño

    private String tipoRelacion; // "PROPIETARIO" o "POSEEDOR"

    private Boolean esResponsablePago; // true por defecto

    private String numeroEscritura;

    // COORDENADAS (Opcionales, por si el predio no está geolocalizado aún)
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double latitud;

    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double longitud;
}

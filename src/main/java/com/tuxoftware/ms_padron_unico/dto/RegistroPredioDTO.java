package com.tuxoftware.ms_padron_unico.dto;

import com.tuxoftware.ms_padron_unico.enums.TipoPredio;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Schema(description = "Información requerida para dar de alta un nuevo predio catastral y asignar su primer propietario.")
public class RegistroPredioDTO {

    // --- DATOS DEL PREDIO ---

    @Schema(description = "Clave Catastral Única (Formato estándar del municipio)", example = "001-002-005-000")
    @NotBlank(message = "La clave catastral es obligatoria")
    private String claveCatastral;

    @Schema(description = "Clave anterior para trazabilidad histórica", example = "ANT-998877")
    private String claveAnterior;

    @Schema(description = "Número de cuenta predial para cobros", example = "CTA-2025-0005")
    private String cuentaPredial;

    @Schema(description = "Clasificación fiscal del predio", example = "URBANO")
    @NotNull(message = "El tipo de predio es obligatorio")
    private TipoPredio tipoPredio;

    @Schema(description = "Uso de suelo registrado", example = "HABITACIONAL")
    private String usoSuelo;

    @Schema(description = "Valor fiscal oficial calculado por Catastro", example = "1250000.50")
    @Positive
    private BigDecimal valorCatastral;

    @Schema(description = "Superficie total del terreno en metros cuadrados", example = "250.00")
    private BigDecimal areaTerrenoM2;

    @Schema(description = "Superficie total construida en metros cuadrados", example = "180.50")
    private BigDecimal areaConstruccionM2;

    // --- DIRECCIÓN ---

    @Schema(description = "Nombre de la vialidad principal", example = "Av. Independencia")
    private String calle;

    @Schema(description = "Número exterior", example = "45-A")
    private String numeroExterior;

    @Schema(description = "Número interior (Depto, Local, etc.)", example = "Int. 2")
    private String numeroInterior;

    @Schema(description = "Colonia, Barrio o Asentamiento", example = "Centro Histórico")
    private String coloniaBarrio;

    @Schema(description = "Código Postal", example = "68300")
    private String codigoPostal;

    // --- DATOS DE LA RELACIÓN (PROPIETARIO INICIAL) ---

    @Schema(description = "UUID del Sujeto Pasivo (Ciudadano/Empresa) que figura como dueño", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    @NotNull(message = "El ID del propietario es obligatorio")
    private UUID sujetoId;

    @Schema(description = "Porcentaje de propiedad sobre el predio (100.00 si es dueño único)", example = "100.00")
    @DecimalMin("0.01")
    @DecimalMax("100.00")
    private BigDecimal porcentajePropiedad;

    @Schema(description = "Naturaleza jurídica de la relación", example = "PROPIETARIO", allowableValues = {"PROPIETARIO", "POSEEDOR"})
    private String tipoRelacion;

    @Schema(description = "Indica si este propietario es quien debe recibir el cobro", example = "true")
    private Boolean esResponsablePago; // true por defecto

    @Schema(description = "Número de escritura pública o título de propiedad", example = "ESC-12345-NOT-5")
    private String numeroEscritura;

    // --- COORDENADAS GEOESPACIALES ---

    @Schema(description = "Latitud geográfica (WGS84)", example = "18.0833")
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double latitud;

    @Schema(description = "Longitud geográfica (WGS84)", example = "-96.1167")
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double longitud;
}

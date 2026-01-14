package com.tuxoftware.ms_padron_unico.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Información para el alta o renovación de una Licencia de Funcionamiento Comercial.")
public record LicenciaComercialDTO(

        @Schema(description = "ID interno del sistema (Null en creación)", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Folio oficial impreso en la licencia", example = "LIC-2025-0500")
        @NotBlank(message = "El número de licencia es obligatorio")
        String numeroLicencia,

        @Schema(description = "UUID del contribuyente titular del negocio", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
        @NotNull(message = "El ID del sujeto pasivo es obligatorio")
        UUID sujetoId,

        @Schema(description = "UUID del predio donde se ubica el establecimiento", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull(message = "El ID del predio es obligatorio")
        UUID predioId,

        @Schema(description = "Nombre público del establecimiento", example = "ABARROTES LA ESQUINITA")
        @NotBlank(message = "El nombre comercial es obligatorio")
        String nombreComercial,

        @Schema(description = "Clave del catálogo de giros (Define la tarifa)", example = "MINISUPER_CON_ALCOHOL")
        @NotBlank(message = "La clave del giro es obligatoria")
        String giroClave,

        @Schema(description = "Detalle adicional de la actividad (Opcional)", example = "Venta de abarrotes, vinos y licores en botella cerrada")
        String giroDescripcion,

        @Schema(description = "Superficie utilizada por el negocio en m2", example = "45.50")
        @NotNull(message = "Los metros cuadrados son obligatorios")
        @Positive(message = "Los metros cuadrados deben ser positivos")
        BigDecimal metrosCuadrados,

        @Schema(description = "Horario de operación autorizado", example = "LUN-SAB 08:00-22:00")
        String horarioFuncionamiento,

        @Schema(description = "Fecha de inicio de operaciones", example = "2025-01-15")
        LocalDate fechaApertura,

        @Schema(description = "Estado administrativo", example = "ACTIVA", allowableValues = {"ACTIVA", "SUSPENDIDA", "EN_TRAMITE"})
        String estadoLicencia,

        @Schema(description = "Número de placa metálica (si aplica)", example = "PLC-9988")
        String numeroPlaca
) {}
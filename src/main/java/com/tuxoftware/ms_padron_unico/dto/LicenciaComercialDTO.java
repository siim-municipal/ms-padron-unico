package com.tuxoftware.ms_padron_unico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LicenciaComercialDTO(
        UUID id, // Null en creación

        @NotBlank(message = "El número de licencia es obligatorio")
        String numeroLicencia,

        @NotNull(message = "El ID del sujeto pasivo es obligatorio")
        UUID sujetoId,

        @NotNull(message = "El ID del predio es obligatorio")
        UUID predioId,

        @NotBlank(message = "El nombre comercial es obligatorio")
        String nombreComercial,

        @NotBlank(message = "La clave del giro es obligatoria")
        String giroClave,

        String giroDescripcion,

        @NotNull(message = "Los metros cuadrados son obligatorios")
        @Positive(message = "Los metros cuadrados deben ser positivos")
        BigDecimal metrosCuadrados,

        String horarioFuncionamiento,
        LocalDate fechaApertura,
        String estadoLicencia,
        String numeroPlaca
) {}
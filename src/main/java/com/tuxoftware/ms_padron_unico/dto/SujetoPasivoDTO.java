package com.tuxoftware.ms_padron_unico.dto;

import com.tuxoftware.ms_padron_unico.enums.TipoPersona;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SujetoPasivoDTO {
    private UUID id;

    @NotNull(message = "El tipo de persona es obligatorio")
    private TipoPersona tipoPersona;

    @NotBlank(message = "El nombre o razón social es obligatorio")
    private String nombreRazonSocial;

    private String apellidoPaterno;
    private String apellidoMaterno;

    @NotBlank(message = "El RFC es obligatorio")
    @Pattern(regexp = "^[A-Z&Ñ]{3,4}[0-9]{6}[A-V1-9][A-Z0-9][0-9A]$", message = "El formato del RFC no es válido")
    private String rfc;

    @Pattern(regexp = "^[A-Z]{4}[0-9]{6}[H,M][A-Z]{5}[0-9,A-Z][0-9]$", message = "El formato de la CURP no es válido")
    private String curp;

    @Email(message = "El formato del correo electrónico no es válido")
    private String email;

    private String telefonoMovil;
    private String direccionFiscal;
}
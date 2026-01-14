package com.tuxoftware.ms_padron_unico.dto;

import com.tuxoftware.ms_padron_unico.enums.TipoPersona;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos del contribuyente (Persona Física o Moral).")
public class SujetoPasivoDTO {

    @Schema(description = "Identificador único (solo lectura, ignorar en creación)", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    private UUID id;

    @Schema(description = "Clasificación fiscal", example = "FISICA", allowableValues = {"FISICA", "MORAL"})
    @NotNull(message = "El tipo de persona es obligatorio")
    private TipoPersona tipoPersona;

    @Schema(description = "Nombre (si es persona física) o Razón Social completa (si es moral)", example = "JUAN")
    @NotBlank(message = "El nombre o razón social es obligatorio")
    private String nombreRazonSocial;

    @Schema(description = "Apellido Paterno (Solo para personas físicas)", example = "PEREZ")
    private String apellidoPaterno;

    @Schema(description = "Apellido Materno (Solo para personas físicas)", example = "LOPEZ")
    private String apellidoMaterno;

    @Schema(description = "Registro Federal de Contribuyentes (Con Homoclave)", example = "PELJ800101H7A")
    @NotBlank(message = "El RFC es obligatorio")
    @Pattern(regexp = "^[A-Z&Ñ]{3,4}[0-9]{6}[A-V1-9][A-Z0-9][0-9A]$", message = "El formato del RFC no es válido")
    private String rfc;

    @Schema(description = "Clave Única de Registro de Población (Solo personas físicas)", example = "PELJ800101HPLRN005")
    @Pattern(regexp = "^[A-Z]{4}[0-9]{6}[H,M][A-Z]{5}[0-9,A-Z][0-9]$", message = "El formato de la CURP no es válido")
    private String curp;

    @Schema(description = "Correo electrónico de contacto para notificaciones", example = "juan.perez@email.com")
    @Email(message = "El formato del correo electrónico no es válido")
    private String email;

    @Schema(description = "Número de celular a 10 dígitos", example = "2871234567")
    private String telefonoMovil;

    @Schema(description = "Domicilio fiscal completo", example = "AV. INDEPENDENCIA 100, COL. CENTRO, TUXTEPEC, OAXACA")
    private String direccionFiscal;
}
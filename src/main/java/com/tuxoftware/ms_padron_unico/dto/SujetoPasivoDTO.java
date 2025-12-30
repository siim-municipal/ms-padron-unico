package com.tuxoftware.ms_padron_unico.dto;


import com.tuxoftware.ms_padron_unico.enums.TipoPersona;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SujetoPasivoDTO {

    private UUID id; // Nulo al crear, lleno al responder

    @NotNull(message = "El tipo de persona es obligatorio")
    private TipoPersona tipoPersona;

    @NotBlank(message = "El nombre o razón social es obligatorio")
    private String nombreRazonSocial;

    private String apellidoPaterno;
    private String apellidoMaterno;

    // TODO: Validaciones basicas
    private String rfc;
    private String curp;
    private String email;
    private String telefonoMovil;
    private String direccionFiscal;
}
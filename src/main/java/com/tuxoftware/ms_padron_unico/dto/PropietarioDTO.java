package com.tuxoftware.ms_padron_unico.dto;

import java.util.UUID;

public record PropietarioDTO(
        UUID id,
        String razonSocial,
        String apellidoPaterno,
        String apellidoMaterno,
        String rfc,
        Boolean esResponsablePago
) {}

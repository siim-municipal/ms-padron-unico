package com.tuxoftware.ms_padron_unico.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record InfoFiscalDTO(
        UUID id,
        BigDecimal valorBase, // Valor Catastral (Predio) o M2 (Licencia)
        String municipioAlias,// La llave de seguridad
        String estatus        // ACTIVO, BAJA, BLOQUEADO
) {}

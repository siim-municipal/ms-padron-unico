package com.tuxoftware.ms_padron_unico.dto;

import com.tuxoftware.ms_padron_unico.enums.TipoPredio;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class PredioListadoDTO {
    private UUID id;
    private String claveCatastral;
    private String claveAnterior;
    private String coloniaBarrio;
    private String calle;
    private String numeroExterior;
    private TipoPredio tipoPredio;
    private Integer ultimoAnioPagado;

    // El campo mágico que el Entity no tiene
    private String propietario;
}

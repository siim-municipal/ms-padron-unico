package com.tuxoftware.ms_padron_unico.dto;

import com.tuxoftware.ms_padron_unico.enums.TipoPredio;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class PredioDetalleDTO {
    private UUID id;
    private String claveCatastral;
    private String claveAnterior;
    private String cuentaPredial;
    private TipoPredio tipoPredio;
    private String usoSuelo;

    // Datos Económicos
    private BigDecimal valorCatastral;
    private BigDecimal areaTerrenoM2;
    private BigDecimal areaConstruccionM2;
    private Integer ultimoAnioPagado;

    // Dirección
    private String calle;
    private String numeroExterior;
    private String numeroInterior;
    private String coloniaBarrio;
    private String codigoPostal;

    // Coordenadas aplanadas
    private Double latitud;
    private Double longitud;

    // Metadatos
    private String fechaRegistro;
    private String createdBy;
    private String lastModifiedBy;
    private String lastModifiedAt;
}
package com.tuxoftware.ms_padron_unico.service;

import com.tuxoftware.ms_padron_unico.dto.LicenciaComercialDTO;
import java.util.UUID;

public interface LicenciaComercialService {

    LicenciaComercialDTO registrarLicencia(LicenciaComercialDTO dto);

    LicenciaComercialDTO buscarPorPlaca(String numeroPlaca);

    LicenciaComercialDTO buscarPorId(UUID id);

    void renovarVigencia(UUID licenciaId, Integer nuevoAnioFiscal);
}

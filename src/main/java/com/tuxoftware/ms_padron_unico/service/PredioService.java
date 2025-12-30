package com.tuxoftware.ms_padron_unico.service;

import com.tuxoftware.ms_padron_unico.dto.RegistroPredioDTO;
import com.tuxoftware.ms_padron_unico.persistence.entity.Predio;

public interface PredioService {
    Predio registrarNuevoPredio(RegistroPredioDTO dto);
}

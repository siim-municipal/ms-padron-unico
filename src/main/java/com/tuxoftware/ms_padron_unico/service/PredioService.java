package com.tuxoftware.ms_padron_unico.service;

import com.tuxoftware.ms_padron_unico.dto.RegistroPredioDTO;
import com.tuxoftware.ms_padron_unico.persistence.entity.Predio;

import java.util.List;

public interface PredioService {

    Predio registrarNuevoPredio(RegistroPredioDTO dto);

    List<Predio> buscarPorCercania(double lat, double lon, double radioMetros);
}

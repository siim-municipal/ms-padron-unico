package com.tuxoftware.ms_padron_unico.service;

import com.tuxoftware.ms_padron_unico.dto.PropietarioDTO;

import java.util.List;
import java.util.UUID;

public interface PropiedadPredioService {

    List<PropietarioDTO> findByPredioId(UUID predioId);

}

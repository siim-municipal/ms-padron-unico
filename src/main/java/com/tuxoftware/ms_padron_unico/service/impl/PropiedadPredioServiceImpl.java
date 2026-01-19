package com.tuxoftware.ms_padron_unico.service.impl;

import com.tuxoftware.ms_padron_unico.dto.PropietarioDTO;
import com.tuxoftware.ms_padron_unico.persistence.repository.PropiedadPredioRepository;
import com.tuxoftware.ms_padron_unico.service.PropiedadPredioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PropiedadPredioServiceImpl implements PropiedadPredioService {
    private final PropiedadPredioRepository propiedadPredioRepository;

    @Override
    public List<PropietarioDTO> findByPredioId(UUID predioId) {
        return propiedadPredioRepository.findResumenByPredioId(predioId);
    }
}

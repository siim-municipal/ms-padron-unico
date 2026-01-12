package com.tuxoftware.ms_padron_unico.service.impl;

import com.tuxoftware.ms_padron_unico.persistence.entity.CatalogoGiro;
import com.tuxoftware.ms_padron_unico.persistence.repository.CatalogoGiroRepository;
import com.tuxoftware.ms_padron_unico.service.CatalogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogoServiceImpl implements CatalogoService {

    private final CatalogoGiroRepository giroRepository;


    @Override
    public List<CatalogoGiro> listarGirosActivos() {
        return giroRepository.findByActivoTrueOrderByDescripcionAsc();
    }
}

package com.tuxoftware.ms_padron_unico.service;

import com.tuxoftware.ms_padron_unico.persistence.entity.CatalogoGiro;

import java.util.List;

public interface CatalogoService {
    List<CatalogoGiro> listarGirosActivos();
}

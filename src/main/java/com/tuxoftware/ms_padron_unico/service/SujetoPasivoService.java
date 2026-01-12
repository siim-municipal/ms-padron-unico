package com.tuxoftware.ms_padron_unico.service;

import com.tuxoftware.ms_padron_unico.dto.SujetoPasivoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SujetoPasivoService {

    SujetoPasivoDTO crearCiudadano(SujetoPasivoDTO dto);

    Page<SujetoPasivoDTO> listarTodo(String busqueda, Pageable pageable);

    SujetoPasivoDTO buscarPorId(UUID id);

    void eliminarCiudadano(UUID id);
}

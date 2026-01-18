package com.tuxoftware.ms_padron_unico.service;

import com.tuxoftware.ms_padron_unico.dto.InfoFiscalDTO;
import com.tuxoftware.ms_padron_unico.dto.PredioDetalleDTO;
import com.tuxoftware.ms_padron_unico.dto.PredioListadoDTO;
import com.tuxoftware.ms_padron_unico.dto.RegistroPredioDTO;
import com.tuxoftware.ms_padron_unico.persistence.entity.Predio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PredioService {

    Predio registrarNuevoPredio(RegistroPredioDTO dto);

    PredioDetalleDTO obtenerDetallePorId(UUID id);

    InfoFiscalDTO obtenerInfoFiscal(UUID id);

    Page<PredioListadoDTO> listarTodos(String busqueda, Pageable pageable);

    List<Predio> buscarPorCercania(double lat, double lon, double radioMetros);

    BigDecimal obtenerValorCatastral(UUID id);

    void actualizarUltimoPago(UUID predioId, Integer anioPagado);
}

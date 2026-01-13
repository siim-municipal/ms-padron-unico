package com.tuxoftware.ms_padron_unico.controller;


import com.tuxoftware.ms_padron_unico.dto.RegistroPredioDTO;
import com.tuxoftware.ms_padron_unico.persistence.entity.Predio;
import com.tuxoftware.ms_padron_unico.service.PredioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/predios")
@RequiredArgsConstructor
public class PredioController {
    private final PredioService predioService;

    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO')") // Solo personal autorizado
    @PostMapping
    public ResponseEntity<Predio> crearPredio(@Valid @RequestBody RegistroPredioDTO dto) {
        Predio nuevoPredio = predioService.registrarNuevoPredio(dto);
        return new ResponseEntity<>(nuevoPredio, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO', 'CATASTRO')")
    @GetMapping("/cercanos")
    public ResponseEntity<java.util.List<Predio>> buscarCercanos(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "500") double distancia) {

        return ResponseEntity.ok(predioService.buscarPorCercania(lat, lon, distancia));
    }

    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO')")
    @GetMapping("/{id}/valor-catastral")
    public ResponseEntity<BigDecimal> obtenerValorCatastral(@PathVariable UUID id) {
        BigDecimal valor = predioService.obtenerValorCatastral(id);
        return ResponseEntity.ok(valor);
    }
}

package com.tuxoftware.ms_padron_unico.controller;

import com.tuxoftware.ms_padron_unico.dto.LicenciaComercialDTO;
import com.tuxoftware.ms_padron_unico.service.LicenciaComercialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/licencias")
@RequiredArgsConstructor
public class LicenciaComercialController {

    private final LicenciaComercialService service;

    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO')")
    @PostMapping
    public ResponseEntity<LicenciaComercialDTO> crearLicencia(@Valid @RequestBody LicenciaComercialDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarLicencia(dto));
    }

    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO', 'INSPECTOR')")
    @GetMapping("/buscar")
    public ResponseEntity<LicenciaComercialDTO> buscarPorPlaca(@RequestParam String placa) {
        return ResponseEntity.ok(service.buscarPorPlaca(placa));
    }
}

package com.tuxoftware.ms_padron_unico.controller;

import com.tuxoftware.ms_padron_unico.dto.SujetoPasivoDTO;
import com.tuxoftware.ms_padron_unico.service.SujetoPasivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sujetos-pasivos")
@RequiredArgsConstructor
public class SujetoPasivoController {
    private final SujetoPasivoService service;

    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO')")
    @PostMapping
    public ResponseEntity<SujetoPasivoDTO> crear(@Valid @RequestBody SujetoPasivoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearCiudadano(dto));
    }

    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO', 'INSPECTOR')")
    @GetMapping
    public ResponseEntity<Page<SujetoPasivoDTO>> listar(
            @RequestParam(required = false) String busqueda,
            @PageableDefault(size = 20, sort = "nombreRazonSocial") Pageable pageable) {
        return ResponseEntity.ok(service.listarTodo(busqueda, pageable));
    }

    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO', 'INSPECTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<SujetoPasivoDTO> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PreAuthorize("hasRole('TESORERO')") // Solo Tesorero puede dar de baja
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        service.eliminarCiudadano(id);
        return ResponseEntity.noContent().build();
    }
}

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
@CrossOrigin(origins = "*") // Para desarrollo. En prod, especifica el dominio de Angular
public class SujetoPasivoController {
    private final SujetoPasivoService service;

    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO')")
    @PostMapping
    public ResponseEntity<SujetoPasivoDTO> crear(@Valid @RequestBody SujetoPasivoDTO dto) {
        return new ResponseEntity<>(service.crearCiudadano(dto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO', 'INSPECTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<SujetoPasivoDTO> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO', 'INSPECTOR')")
    @GetMapping
    public ResponseEntity<Page<SujetoPasivoDTO>> listar(
            @PageableDefault(size = 20, sort = "nombreRazonSocial") Pageable pageable) {
        return ResponseEntity.ok(service.listarTodo(pageable));
    }
}

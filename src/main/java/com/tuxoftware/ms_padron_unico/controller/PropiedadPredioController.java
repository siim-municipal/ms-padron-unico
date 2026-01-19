package com.tuxoftware.ms_padron_unico.controller;

import com.tuxoftware.ms_padron_unico.dto.PropietarioDTO;
import com.tuxoftware.ms_padron_unico.service.PropiedadPredioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/propiedades")
@RequiredArgsConstructor
@Tag(name = "Relación Predio-Sujeto", description = "Gestión de propietarios y poseedores")
public class PropiedadPredioController {

    private final PropiedadPredioService propiedadPredioService;

    @Operation(summary = "Obtener propietarios de un predio")
    @GetMapping("/por-predio/{predioId}")
    public ResponseEntity<List<PropietarioDTO>> getPropietarios(@PathVariable UUID predioId) {
        return ResponseEntity.ok(propiedadPredioService.findByPredioId(predioId));
    }
}
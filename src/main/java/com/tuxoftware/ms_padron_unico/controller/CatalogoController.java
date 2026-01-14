package com.tuxoftware.ms_padron_unico.controller;

import com.tuxoftware.ms_padron_unico.persistence.entity.CatalogoGiro;
import com.tuxoftware.ms_padron_unico.service.CatalogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogos")
@RequiredArgsConstructor
@Tag(name = "Catálogos del Sistema", description = "Listados maestros de solo lectura para poblar formularios en el Frontend.")
public class CatalogoController {

    private final CatalogoService catalogoService;

    @Operation(summary = "Listar Giros Comerciales Activos", description = "Devuelve la lista de actividades económicas permitidas por el municipio.")
    @ApiResponse(
            responseCode = "200",
            description = "Lista recuperada exitosamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CatalogoGiro.class)))
    )
    @GetMapping("/giros")
    public ResponseEntity<List<CatalogoGiro>> listarGiros() {
        return ResponseEntity.ok(catalogoService.listarGirosActivos());
    }
}

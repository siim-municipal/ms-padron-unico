package com.tuxoftware.ms_padron_unico.controller;

import com.tuxoftware.ms_padron_unico.dto.SujetoPasivoDTO;
import com.tuxoftware.ms_padron_unico.service.SujetoPasivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
@Tag(name = "Padrón - Ciudadanos y Empresas", description = "Gestión del directorio de contribuyentes (Sujetos Pasivos). Incluye personas físicas y morales.")
public class SujetoPasivoController {

    private final SujetoPasivoService service;

    @Operation(summary = "Registrar un nuevo contribuyente", description = "Crea un registro de persona física o moral. Valida RFC y CURP únicos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contribuyente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o duplicidad en RFC/CURP", content = @Content),
            @ApiResponse(responseCode = "403", description = "No autorizado (Requiere rol TESORERO o CAJERO)", content = @Content)
    })
    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO')")
    @PostMapping
    public ResponseEntity<SujetoPasivoDTO> crear(@Valid @RequestBody SujetoPasivoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearCiudadano(dto));
    }

    @Operation(summary = "Listar contribuyentes con paginación", description = "Recupera el padrón de ciudadanos filtrando por nombre o razón social.")
    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO', 'INSPECTOR')")
    @GetMapping
    public ResponseEntity<Page<SujetoPasivoDTO>> listar(
            @Parameter(description = "Texto para filtrar por nombre, apellidos o razón social", example = "JUAN PEREZ")
            @RequestParam(required = false) String busqueda,

            @ParameterObject
            @PageableDefault(size = 20, sort = "nombreRazonSocial") Pageable pageable) {
        return ResponseEntity.ok(service.listarTodo(busqueda, pageable));
    }

    @Operation(summary = "Obtener contribuyente por ID", description = "Devuelve el detalle completo de un sujeto pasivo específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Sujeto pasivo no encontrado")
    })
    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO', 'INSPECTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<SujetoPasivoDTO> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar si ya existe por RFC", description = "Retorna boolean si existe el rfc buscado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Eliminado correctamente"),
            @ApiResponse(responseCode = "403", description = "Permisos insuficientes (Solo TESORERO, CAJERO)")
    })
    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO')")
    @GetMapping("/existe/{rfc}")
    public ResponseEntity<Boolean> existeRfc(@PathVariable String rfc) {
        boolean existe = service.existePorRfc(rfc);
        return ResponseEntity.ok(existe);
    }

    @Operation(summary = "Dar de baja contribuyente (Borrado Lógico)", description = "Cambia el estatus del registro a INACTIVO. Solo el Tesorero puede realizar esta acción.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminado correctamente"),
            @ApiResponse(responseCode = "403", description = "Permisos insuficientes (Solo TESORERO)")
    })
    @PreAuthorize("hasRole('TESORERO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        service.eliminarCiudadano(id);
        return ResponseEntity.noContent().build();
    }
}

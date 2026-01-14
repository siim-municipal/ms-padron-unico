package com.tuxoftware.ms_padron_unico.controller;

import com.tuxoftware.ms_padron_unico.dto.LicenciaComercialDTO;
import com.tuxoftware.ms_padron_unico.service.LicenciaComercialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/licencias")
@RequiredArgsConstructor
@Tag(name = "Padrón - Licencias Comerciales", description = "Emisión, renovación y consulta de licencias de funcionamiento para establecimientos.")
public class LicenciaComercialController {

    private final LicenciaComercialService service;

    @Operation(
            summary = "Registrar nueva Licencia",
            description = "Emite una nueva licencia comercial vinculando un Predio con un Sujeto Pasivo y un Giro específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Licencia emitida correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej. Predio no existe o Giro incorrecto)"),
            @ApiResponse(responseCode = "409", description = "Ya existe una licencia activa para ese local/giro")
    })
    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO')")
    @PostMapping
    public ResponseEntity<LicenciaComercialDTO> crearLicencia(@Valid @RequestBody LicenciaComercialDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarLicencia(dto));
    }

    @Operation(
            summary = "Buscar Licencia por Placa",
            description = "Consulta rápida para inspectores. Verifica la validez de una licencia mediante su número de placa física."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Licencia encontrada"),
            @ApiResponse(responseCode = "404", description = "Placa no registrada o licencia dada de baja")
    })
    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO', 'INSPECTOR')")
    @GetMapping("/buscar")
    public ResponseEntity<LicenciaComercialDTO> buscarPorPlaca(
            @Parameter(description = "Número de la placa metálica o folio visible", example = "PLC-2025-001")
            @RequestParam String placa) {
        return ResponseEntity.ok(service.buscarPorPlaca(placa));
    }

    @Operation(
            summary = "Renovar vigencia fiscal (Uso interno Tesorería)",
            description = "Actualiza el año fiscal cubierto tras la confirmación del pago. Endpoint de integración backend-to-backend."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Renovación aplicada correctamente"),
            @ApiResponse(responseCode = "404", description = "Licencia no encontrada")
    })
    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO')")
    @PatchMapping("/{licenciaId}/renovacion")
    public ResponseEntity<Void> actualizarLicencia(
            @Parameter(description = "UUID de la licencia") @PathVariable UUID licenciaId,
            @Parameter(description = "Año fiscal pagado", example = "2025") @RequestParam Integer anioFiscal) {

        service.renovarVigencia(licenciaId, anioFiscal);
        return ResponseEntity.noContent().build();
    }
}

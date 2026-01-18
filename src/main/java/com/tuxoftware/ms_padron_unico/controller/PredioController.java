package com.tuxoftware.ms_padron_unico.controller;

import com.tuxoftware.ms_padron_unico.dto.InfoFiscalDTO;
import com.tuxoftware.ms_padron_unico.dto.PredioDetalleDTO;
import com.tuxoftware.ms_padron_unico.dto.PredioListadoDTO;
import com.tuxoftware.ms_padron_unico.dto.RegistroPredioDTO;
import com.tuxoftware.ms_padron_unico.persistence.entity.Predio;
import com.tuxoftware.ms_padron_unico.service.PredioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/predios")
@RequiredArgsConstructor
@Tag(name = "Gestión de Padrón y Predios", description = "Operaciones CRUD y geoespaciales sobre el inventario catastral del municipio.")
public class PredioController {

    private final PredioService predioService;

    @Operation(
            summary = "Registrar un nuevo predio",
            description = "Da de alta un predio en el sistema catastral. Requiere rol TESORERO o CAJERO. Valida duplicidad de clave catastral."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Predio creado exitosamente",
                    content = @Content(schema = @Schema(implementation = Predio.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o Clave Catastral duplicada", content = @Content),
            @ApiResponse(responseCode = "403", description = "No tiene permisos suficientes", content = @Content)
    })
    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO')")
    @PostMapping
    public ResponseEntity<Predio> crearPredio(@Valid @RequestBody RegistroPredioDTO dto) {
        Predio nuevoPredio = predioService.registrarNuevoPredio(dto);
        return new ResponseEntity<>(nuevoPredio, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Búsqueda geoespacial de predios",
            description = "Localiza predios dentro de un radio específico basado en coordenadas GPS. Útil para inspectores en campo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de predios encontrados (puede estar vacía)"),
            @ApiResponse(responseCode = "400", description = "Coordenadas inválidas")
    })
    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO', 'CATASTRO')")
    @GetMapping("/cercanos")
    public ResponseEntity<List<Predio>> buscarCercanos(
            @Parameter(description = "Latitud decimal", example = "18.0833")
            @RequestParam double lat,

            @Parameter(description = "Longitud decimal", example = "-96.1167")
            @RequestParam double lon,

            @Parameter(description = "Radio de búsqueda en metros", example = "500")
            @RequestParam(defaultValue = "500") double distancia) {

        return ResponseEntity.ok(predioService.buscarPorCercania(lat, lon, distancia));
    }

    @Operation(summary = "Consultar Valor Catastral", description = "Obtiene el valor oficial del predio para cálculos de impuestos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Valor retornado correctamente"),
            @ApiResponse(responseCode = "404", description = "Predio no encontrado")
    })
    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO')")
    @GetMapping("/{id}/valor-catastral")
    public ResponseEntity<BigDecimal> obtenerValorCatastral(
            @Parameter(description = "UUID del predio", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {

        BigDecimal valor = predioService.obtenerValorCatastral(id);
        return ResponseEntity.ok(valor);
    }

    @Operation(
            summary = "Obtener detalle completo de un predio",
            description = "Devuelve la información detallada del predio por su UUID. Incluye coordenadas formateadas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Predio encontrado",
                    content = @Content(schema = @Schema(implementation = PredioDetalleDTO.class))),
            @ApiResponse(responseCode = "404", description = "El predio no existe"),
            @ApiResponse(responseCode = "400", description = "UUID inválido")
    })
    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO', 'CATASTRO')")
    @GetMapping("/{id}")
    public ResponseEntity<PredioDetalleDTO> obtenerPorId(
            @Parameter(description = "UUID del predio", required = true)
            @PathVariable UUID id) {

        return ResponseEntity.ok(predioService.obtenerDetallePorId(id));
    }

    @Operation(summary = "Obtener Info Fiscal (Sistema a Sistema)",
            description = "Endpoint ligero para validación de seguridad y base gravable. Uso exclusivo de ms-calculos.")
    @GetMapping("/{id}/info-fiscal")
    public ResponseEntity<InfoFiscalDTO> obtenerInfoPredio(
            @Parameter(description = "UUID del predio") @PathVariable UUID id) {

        return ResponseEntity.ok(predioService.obtenerInfoFiscal(id));
    }

    @Operation(summary = "Listar predios paginados",
            description = "Busca por Clave o Colonia e incluye el nombre del propietario.")
    @PreAuthorize("hasAnyRole('TESORERO', 'CAJERO', 'CATASTRO')")
    @GetMapping
    public ResponseEntity<Page<PredioListadoDTO>> listarPredios(
            @RequestParam(required = false) String busqueda,
            @PageableDefault(sort = "claveCatastral") Pageable pageable) {

        return ResponseEntity.ok(predioService.listarTodos(busqueda, pageable));
    }

    // Endpoint de integración (Outbox Pattern receiver)
    @Operation(
            summary = "Actualizar historial de pagos (Sistema a Sistema)",
            description = "Endpoint exclusivo para uso interno por ms-tesoreria-recaudacion. Actualiza la vigencia fiscal del predio tras un pago exitoso."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Actualización correcta (No Content)"),
            @ApiResponse(responseCode = "404", description = "Predio no encontrado")
    })
    @PatchMapping("/{predioId}/historial-pagos")
    public ResponseEntity<Void> actualizarUltimoPago(
            @Parameter(description = "UUID del predio a actualizar") @PathVariable UUID predioId,
            @Parameter(description = "Año fiscal que se acaba de liquidar", example = "2025") @RequestParam Integer anioPagado) {

        predioService.actualizarUltimoPago(predioId, anioPagado);
        return ResponseEntity.noContent().build();
    }
}

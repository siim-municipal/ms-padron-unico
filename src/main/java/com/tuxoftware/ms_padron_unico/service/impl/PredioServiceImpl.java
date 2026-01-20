package com.tuxoftware.ms_padron_unico.service.impl;

import com.tuxoftware.ms_padron_unico.dto.InfoFiscalDTO;
import com.tuxoftware.ms_padron_unico.dto.PredioDetalleDTO;
import com.tuxoftware.ms_padron_unico.dto.PredioListadoDTO;
import com.tuxoftware.ms_padron_unico.dto.RegistroPredioDTO;
import com.tuxoftware.ms_padron_unico.mapper.PredioMapper;
import com.tuxoftware.ms_padron_unico.persistence.entity.Predio;
import com.tuxoftware.ms_padron_unico.persistence.entity.PropiedadPredio;
import com.tuxoftware.ms_padron_unico.persistence.entity.SujetoPasivo;
import com.tuxoftware.ms_padron_unico.persistence.repository.PredioRepository;
import com.tuxoftware.ms_padron_unico.persistence.repository.PropiedadPredioRepository;
import com.tuxoftware.ms_padron_unico.persistence.repository.SujetoPasivoRepository;
import com.tuxoftware.ms_padron_unico.service.PredioService;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PredioServiceImpl implements PredioService {
    private final PredioRepository predioRepository;
    private final SujetoPasivoRepository sujetoRepository;
    private final PropiedadPredioRepository propiedadPredioRepository;

    private final PredioMapper predioMapper;

    @Transactional
    public Predio registrarNuevoPredio(RegistroPredioDTO dto) {

        // 1. Validar reglas de negocio
        if (predioRepository.existsByClaveCatastral(dto.getClaveCatastral())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La clave catastral " + dto.getClaveCatastral() + " ya existe.");
        }

        // 2. Buscar dependencias
        SujetoPasivo sujeto = sujetoRepository.findById(dto.getSujetoId())
                .orElseThrow(() -> new EntityNotFoundException("El sujeto pasivo no existe"));

        // 3. Crear Predio usando Mapper
        Predio predio = predioMapper.toPredioEntity(dto);
        // Opcional: Si necesitas lógica extra que el mapper no cubre (ej. ubicación geoespacial compleja)
        // predio.setUbicacionCentro(...);

        Predio predioGuardado = predioRepository.save(predio);

        // 4. Crear Relación usando Mapper
        PropiedadPredio relacion = predioMapper.toRelacionEntity(dto, sujeto, predioGuardado);

        propiedadPredioRepository.save(relacion);

        return predioGuardado;
    }

    @Override
    @Transactional(readOnly = true)
    public PredioDetalleDTO obtenerDetallePorId(UUID id) {
        Predio predio = predioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Predio no encontrado"));

        return predioMapper.toDetalleDTO(predio);
    }

    @Override
    @Transactional(readOnly = true)
    public InfoFiscalDTO obtenerInfoFiscal(UUID id) {
        Predio predio = predioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Predio no encontrado: " + id));

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String alias = jwt.getClaimAsString("municipio_id");

        return new InfoFiscalDTO(
                predio.getId(),
                predio.getValorCatastral(), // Base gravable
                alias,    // Vital para Tenant Isolation
                predio.getEstatus().name()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PredioListadoDTO> listarTodos(String busqueda, Pageable pageable) {

        // 1. Crear Specification para filtrar (Búsqueda dinámica)
        Specification<Predio> spec = (root, query, cb) -> {
            if (busqueda == null || busqueda.isBlank()) {
                return cb.conjunction(); // Sin filtro
            }
            String likePattern = "%" + busqueda.toUpperCase() + "%";
            return cb.or(
                    cb.like(cb.upper(root.get("claveCatastral")), likePattern),
                    cb.like(cb.upper(root.get("coloniaBarrio")), likePattern),
                    cb.like(cb.upper(root.get("claveAnterior")), likePattern)
            );
        };

        // 2. Obtener la página de Entidades (Solo datos del predio)
        Page<Predio> predioPage = predioRepository.findAll(spec, pageable);

        if (predioPage.isEmpty()) {
            return Page.empty(pageable);
        }

        // 3. Obtener los IDs de los predios encontrados en esta página
        List<UUID> predioIds = predioPage.getContent().stream()
                .map(Predio::getId)
                .toList();

        // 4. Buscar los dueños responsables SOLO para estos predios (Optimización N+1)
        List<PropiedadPredio> propiedades = propiedadPredioRepository.findResponsablesPorPredioIds(predioIds);

        // 5. Crear un Mapa para acceso rápido: PredioID -> Nombre Dueño
        Map<UUID, String> mapaPropietarios = propiedades.stream()
                .collect(Collectors.toMap(
                        p -> p.getPredio().getId(),
                        p -> p.getSujeto().getNombreRazonSocial() +
                                (p.getSujeto().getApellidoPaterno() != null ? " " + p.getSujeto().getApellidoPaterno() : ""),
                        (existente, reemplazo) -> existente // Si hay duplicados (error de datos), nos quedamos el primero
                ));

        // 6. Mapear Entity -> DTO y asignar el propietario
        return predioPage.map(predio -> {
            String nombrePropietario = mapaPropietarios.getOrDefault(predio.getId(), "SIN PROPIETARIO ASIGNADO");

            return PredioListadoDTO.builder()
                    .id(predio.getId())
                    .claveCatastral(predio.getClaveCatastral())
                    .claveAnterior(predio.getClaveAnterior())
                    .coloniaBarrio(predio.getColoniaBarrio())
                    .calle(predio.getCalle())
                    .numeroExterior(predio.getNumeroExterior())
                    .tipoPredio(predio.getTipoPredio())
                    .ultimoAnioPagado(predio.getUltimoAnioPagado())
                    .propietario(nombrePropietario)
                    .build();
        });
    }


    @Transactional(readOnly = true)
    @Override
    public List<Predio> buscarPorCercania(double lat, double lon, double radioMetros) {
        return predioRepository.buscarCercanos(lon, lat, radioMetros);
    }

    @Transactional(readOnly = true)
    @Override
    public BigDecimal obtenerValorCatastral(UUID id) {
        return predioRepository.findById(id)
                .map(Predio::getValorCatastral)
                .orElseThrow(() -> new EntityNotFoundException("Predio no encontrado: " + id));
    }

    @Override
    public Boolean existePredioPorId(UUID id) {
        return predioRepository.findById(id).isPresent();
    }

    @Transactional
    @Override
    public void actualizarUltimoPago(UUID predioId, Integer anioPagado) {
        Predio predio = predioRepository.findById(predioId)
                .orElseThrow(() -> new RuntimeException("Predio no encontrado: " + predioId));

        // Solo actualizamos si el año pagado es mayor al actual (evitar regresiones)
        // O si estaba nulo (nunca había pagado)
        Integer actual = predio.getUltimoAnioPagado() != null ? predio.getUltimoAnioPagado() : 0;

        if (anioPagado > actual) {
            predio.setUltimoAnioPagado(anioPagado);
            predioRepository.save(predio);
            log.info("Predio {} actualizado. Último año pagado: {}", predioId, anioPagado);
        } else {
            log.warn("Ignorando actualización de pago para predio {}. Año recibido ({}) es menor o igual al actual ({})",
                    predioId, anioPagado, actual);
        }
    }
}

package com.tuxoftware.ms_padron_unico.service.impl;

import com.tuxoftware.ms_padron_unico.dto.SujetoPasivoDTO;
import com.tuxoftware.ms_padron_unico.enums.EstatusRegistro;
import com.tuxoftware.ms_padron_unico.enums.TipoPersona;
import com.tuxoftware.ms_padron_unico.mapper.SujetoPasivoMapper;
import com.tuxoftware.ms_padron_unico.persistence.entity.SujetoPasivo;
import com.tuxoftware.ms_padron_unico.persistence.repository.SujetoPasivoRepository;
import com.tuxoftware.ms_padron_unico.service.SujetoPasivoService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SujetoPasivoImpl implements SujetoPasivoService {
    private final SujetoPasivoRepository repository;
    private final SujetoPasivoMapper mapper;

    @Override
    @Transactional
    public SujetoPasivoDTO crearCiudadano(SujetoPasivoDTO dto) {
        // 1. Validar RFC único
        if (repository.existsByRfc(dto.getRfc())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El RFC ya está registrado: " + dto.getRfc());
        }

        // 2. Validación Condicional: CURP obligatoria para Físicas
        if (TipoPersona.FISICA.equals(dto.getTipoPersona()) && (dto.getCurp() == null || dto.getCurp().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La CURP es obligatoria para personas físicas");
        }

        SujetoPasivo entidad = mapper.toEntity(dto);
        entidad.setEstatus(EstatusRegistro.ACTIVO); // Asegurar estatus inicial
        return mapper.toDTO(repository.save(entidad));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SujetoPasivoDTO> listarTodo(String busqueda, Pageable pageable) {
        Specification<SujetoPasivo> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Filtro por estatus activo
            predicates.add(cb.equal(root.get("estatus"), EstatusRegistro.ACTIVO));

            if (busqueda != null && !busqueda.isBlank()) {
                String likePattern = "%" + busqueda.toUpperCase() + "%";
                // Buscar por Nombre, RFC o Razón Social
                Predicate porNombre = cb.like(cb.upper(root.get("nombreRazonSocial")), likePattern);
                Predicate porRfc = cb.like(cb.upper(root.get("rfc")), likePattern);
                predicates.add(cb.or(porNombre, porRfc));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return repository.findAll(spec, pageable).map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public SujetoPasivoDTO buscarPorId(UUID id) {
        return repository.findById(id)
                .filter(s -> EstatusRegistro.ACTIVO.equals(s.getEstatus()))
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contribuyente no encontrado o inactivo"));
    }

    @Override
    public boolean existePorRfc(String rfc) {
        return repository.existsByRfc(rfc);
    }

    @Override
    @Transactional
    public void eliminarCiudadano(UUID id) {
        SujetoPasivo entidad = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contribuyente no encontrado"));

        entidad.setEstatus(EstatusRegistro.BAJA);
        repository.save(entidad);
    }
}

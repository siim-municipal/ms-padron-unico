package com.tuxoftware.ms_padron_unico.service.impl;

import com.tuxoftware.ms_padron_unico.dto.SujetoPasivoDTO;
import com.tuxoftware.ms_padron_unico.mapper.SujetoPasivoMapper;
import com.tuxoftware.ms_padron_unico.persistence.entity.SujetoPasivo;
import com.tuxoftware.ms_padron_unico.persistence.repository.SujetoPasivoRepository;
import com.tuxoftware.ms_padron_unico.service.SujetoPasivoService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class SujetoPasivoImpl implements SujetoPasivoService {
    private final SujetoPasivoRepository repository;
    private final SujetoPasivoMapper mapper;

    @Transactional
    public SujetoPasivoDTO crearCiudadano(SujetoPasivoDTO dto) {
        // 1. Regla de Negocio: Validar RFC único
        if (dto.getRfc() != null && repository.findByRfc(dto.getRfc()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un contribuyente con ese RFC");
        }

        // 2. Convertir DTO a Entidad (Mapping manual por ahora)
        SujetoPasivo entidad = mapper.toEntity(dto);

        // 3. Guardar
        SujetoPasivo guardado = repository.save(entidad);

        // 4. Convertir de vuelta a DTO
        return mapper.toDTO(guardado);
    }

    @Transactional(readOnly = true)
    public Page<SujetoPasivoDTO> listarTodo(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public SujetoPasivoDTO buscarPorId(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contribuyente no encontrado"));
    }


}

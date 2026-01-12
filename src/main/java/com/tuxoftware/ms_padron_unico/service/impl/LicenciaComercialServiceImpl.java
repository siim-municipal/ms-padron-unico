package com.tuxoftware.ms_padron_unico.service.impl;

import com.tuxoftware.ms_padron_unico.dto.LicenciaComercialDTO;
import com.tuxoftware.ms_padron_unico.mapper.LicenciaMapper;
import com.tuxoftware.ms_padron_unico.persistence.entity.CatalogoGiro;
import com.tuxoftware.ms_padron_unico.persistence.entity.LicenciaComercial;
import com.tuxoftware.ms_padron_unico.persistence.entity.Predio;
import com.tuxoftware.ms_padron_unico.persistence.entity.SujetoPasivo;
import com.tuxoftware.ms_padron_unico.persistence.repository.CatalogoGiroRepository;
import com.tuxoftware.ms_padron_unico.persistence.repository.LicenciaComercialRepository;
import com.tuxoftware.ms_padron_unico.persistence.repository.PredioRepository;
import com.tuxoftware.ms_padron_unico.persistence.repository.SujetoPasivoRepository;
import com.tuxoftware.ms_padron_unico.service.LicenciaComercialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LicenciaComercialServiceImpl implements LicenciaComercialService {

    private final LicenciaComercialRepository licenciaRepository;
    private final SujetoPasivoRepository sujetoRepository;
    private final PredioRepository predioRepository;
    private final LicenciaMapper mapper;
    private final CatalogoGiroRepository giroRepository;

    @Override
    @Transactional
    public LicenciaComercialDTO registrarLicencia(LicenciaComercialDTO dto) {
        // 1. Validar Unicidad de Licencia
        if (licenciaRepository.existsByNumeroLicencia(dto.numeroLicencia())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El número de licencia ya existe: " + dto.numeroLicencia());
        }

        // 2. Validar Existencia de Dependencias
        SujetoPasivo sujeto = sujetoRepository.findById(dto.sujetoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sujeto Pasivo no encontrado"));

        Predio predio = predioRepository.findById(dto.predioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Predio no encontrado"));

        boolean giroValido = giroRepository.existsById(dto.giroClave());
        if (!giroValido) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El giro comercial '" + dto.giroClave() + "' no es válido en el catálogo actual.");
        }

        // 3. Persistir
        LicenciaComercial entity = mapper.toEntity(dto, sujeto, predio);

        // Regla de negocio: Si no envían estado, por defecto es EN_TRAMITE o ACTIVA según lógica
        if (entity.getEstadoLicencia() == null) {
            entity.setEstadoLicencia("ACTIVA");
        }

        return mapper.toDTO(licenciaRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public LicenciaComercialDTO buscarPorPlaca(String numeroPlaca) {
        return licenciaRepository.findByNumeroPlaca(numeroPlaca)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Licencia no encontrada con placa: " + numeroPlaca));
    }

    @Override
    @Transactional(readOnly = true)
    public LicenciaComercialDTO buscarPorId(UUID id) {
        return licenciaRepository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Licencia no encontrada"));
    }
}

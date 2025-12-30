package com.tuxoftware.ms_padron_unico.service.impl;

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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PredioImpl implements PredioService {
    private final PredioRepository predioRepository;
    private final SujetoPasivoRepository sujetoRepository;
    private final PropiedadPredioRepository propiedadPredioRepository;

    private final PredioMapper predioMapper;

    @Transactional
    public Predio registrarNuevoPredio(RegistroPredioDTO dto) {

        // 1. Validar reglas de negocio
        if (predioRepository.existsByClaveCatastral(dto.getClaveCatastral())) {
            throw new IllegalArgumentException("La clave catastral " + dto.getClaveCatastral() + " ya existe.");
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
}

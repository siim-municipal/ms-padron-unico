package com.tuxoftware.ms_padron_unico.mapper;

import com.tuxoftware.ms_padron_unico.dto.SujetoPasivoDTO;
import com.tuxoftware.ms_padron_unico.persistence.entity.SujetoPasivo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SujetoPasivoMapper {

    // Convierte de Entidad a DTO
    SujetoPasivoDTO toDTO(SujetoPasivo entity);

    // Convierte de DTO a Entidad
    // Ignoramos 'id' y auditoría al crear desde cero para que la BD los genere
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "propiedades", ignore = true) // Ignoramos relaciones complejas por ahora
    SujetoPasivo toEntity(SujetoPasivoDTO dto);

    // Method útil para ACTUALIZACIONES (PUT/PATCH)
    // Actualiza una entidad existente con datos del DTO, sin sobreescribir el ID
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "propiedades", ignore = true)
    void updateEntityFromDTO(SujetoPasivoDTO dto, @MappingTarget SujetoPasivo entity);
}
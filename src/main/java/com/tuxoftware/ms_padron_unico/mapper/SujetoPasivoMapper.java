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

    // CREACIÓN
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "propiedades", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    SujetoPasivo toEntity(SujetoPasivoDTO dto);

    // ACTUALIZACIÓN
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "propiedades", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    void updateEntityFromDTO(SujetoPasivoDTO dto, @MappingTarget SujetoPasivo entity);
}
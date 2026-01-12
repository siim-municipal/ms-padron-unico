package com.tuxoftware.ms_padron_unico.mapper;

import com.tuxoftware.ms_padron_unico.dto.LicenciaComercialDTO;
import com.tuxoftware.ms_padron_unico.persistence.entity.LicenciaComercial;
import com.tuxoftware.ms_padron_unico.persistence.entity.Predio;
import com.tuxoftware.ms_padron_unico.persistence.entity.SujetoPasivo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LicenciaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estatus", constant = "ACTIVO")
    @Mapping(target = "sujeto", source = "sujeto")
    @Mapping(target = "predio", source = "predio")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LicenciaComercial toEntity(LicenciaComercialDTO dto, SujetoPasivo sujeto, Predio predio);

    @Mapping(target = "sujetoId", source = "sujeto.id")
    @Mapping(target = "predioId", source = "predio.id")
    LicenciaComercialDTO toDTO(LicenciaComercial entity);
}

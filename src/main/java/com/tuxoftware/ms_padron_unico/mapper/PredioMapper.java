package com.tuxoftware.ms_padron_unico.mapper;

import com.tuxoftware.ms_padron_unico.dto.RegistroPredioDTO;
import com.tuxoftware.ms_padron_unico.persistence.entity.Predio;
import com.tuxoftware.ms_padron_unico.persistence.entity.PropiedadPredio;
import com.tuxoftware.ms_padron_unico.persistence.entity.SujetoPasivo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.time.LocalDate;

@Mapper(
        componentModel = "spring", // Permite inyectarlo con @Autowired
        unmappedTargetPolicy = ReportingPolicy.IGNORE, // Ignora campos que no coincidan (id, auditoría)
        imports = {LocalDate.class, BigDecimal.class} // Para usar en expressions
)
public interface PredioMapper {
    // 1. Mapeo de DTO a Entidad Predio
    // Los campos con el mismo nombre se mapean solos (calle, claveCatastral, etc.)
    Predio toPredioEntity(RegistroPredioDTO dto);

    // 2. Mapeo de DTO a Relación PropiedadPredio
    // Necesitamos pasar el DTO, el Sujeto ya buscado y el Predio ya guardado
    @Mapping(target = "id", ignore = true) // Se genera automático
    @Mapping(target = "sujeto", source = "sujeto")
    @Mapping(target = "predio", source = "predio")
    // Valores por defecto si vienen nulos en el DTO
    @Mapping(target = "tipoRelacion", source = "dto.tipoRelacion", defaultValue = "PROPIETARIO")
    @Mapping(target = "porcentajePropiedad", source = "dto.porcentajePropiedad", defaultValue = "100.00")
    @Mapping(target = "esResponsablePago", source = "dto.esResponsablePago", defaultValue = "true")
    // Lógica para fecha actual
    @Mapping(target = "fechaEscrituracion", expression = "java(LocalDate.now())")
    PropiedadPredio toRelacionEntity(RegistroPredioDTO dto, SujetoPasivo sujeto, Predio predio);
}

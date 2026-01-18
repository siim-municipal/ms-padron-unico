package com.tuxoftware.ms_padron_unico.mapper;

import com.tuxoftware.ms_padron_unico.dto.PredioDetalleDTO;
import com.tuxoftware.ms_padron_unico.dto.RegistroPredioDTO;
import com.tuxoftware.ms_padron_unico.persistence.entity.Predio;
import com.tuxoftware.ms_padron_unico.persistence.entity.PropiedadPredio;
import com.tuxoftware.ms_padron_unico.persistence.entity.SujetoPasivo;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.time.LocalDate;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {LocalDate.class, BigDecimal.class}
)
public interface PredioMapper {

    // 1. Mapeo principal
    @Mapping(target = "ubicacionCentro", source = "dto", qualifiedByName = "mapCoordinatesToPoint")
    Predio toPredioEntity(RegistroPredioDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sujeto", source = "sujeto")
    @Mapping(target = "predio", source = "predio")
    @Mapping(target = "tipoRelacion", source = "dto.tipoRelacion", defaultValue = "PROPIETARIO")
    @Mapping(target = "porcentajePropiedad", source = "dto.porcentajePropiedad", defaultValue = "100.00")
    @Mapping(target = "esResponsablePago", source = "dto.esResponsablePago", defaultValue = "true")
    @Mapping(target = "fechaEscrituracion", expression = "java(LocalDate.now())")
    PropiedadPredio toRelacionEntity(RegistroPredioDTO dto, SujetoPasivo sujeto, Predio predio);

    @Mapping(target = "latitud", source = "ubicacionCentro", qualifiedByName = "extractLat")
    @Mapping(target = "longitud", source = "ubicacionCentro", qualifiedByName = "extractLon")
    // Fechas
    @Mapping(target = "fechaRegistro", expression = "java(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null)")
    @Mapping(target = "lastModifiedAt", expression = "java(entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null)")
    // Usuarios
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    PredioDetalleDTO toDetalleDTO(Predio entity);

    // LÓGICA GEOESPACIAL PERSONALIZADA

    @Named("mapCoordinatesToPoint")
    default Point mapCoordinatesToPoint(RegistroPredioDTO dto) {
        if (dto.getLatitud() == null || dto.getLongitud() == null) {
            return null;
        }
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(dto.getLongitud(), dto.getLatitud());
        return geometryFactory.createPoint(coordinate);
    }

    @Named("extractLat")
    default Double extractLat(Point point) {
        if (point == null) return null;
        return point.getY(); // Y es Latitud
    }

    @Named("extractLon")
    default Double extractLon(Point point) {
        if (point == null) return null;
        return point.getX();
    }
}

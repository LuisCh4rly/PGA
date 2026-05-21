package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.ResponseDto.DetalleDashboardDto;
import com.app.pga.App.Models.Entities.DetalleSeguimiento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)

public interface DetalleSeguimientoMapper {
    @Mapping(target = "tituloActividad", source = "actividad.actividadGrupo.titulo")
    @Mapping(target = "avanceGlobalActividad", source = "actividad.avanceGlobal")
    @Mapping(target = "idActividadAlumno", source = "actividad.idActividadAlumno")
    @Mapping(target = "requiereEntrega", source = "actividad.actividadGrupo.reqEntrega")
    @Mapping(target = "idSeguimientoSemanal", source = "semana.idSeguimientoSemanal")
    DetalleDashboardDto toDashboardDto (DetalleSeguimiento dto);


}

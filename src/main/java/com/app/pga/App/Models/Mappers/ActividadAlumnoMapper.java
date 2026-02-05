package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.ActividadAlumnoDto;
import com.app.pga.App.Models.Entities.ActividadAlumno;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActividadAlumnoMapper {
    ActividadAlumno toEntity(ActividadAlumnoDto actividadAlumnoDto);

    @Mapping(target = "idInscripcion", source = "inscripcion.idInscripcion")
    @Mapping(target = "idActividadGrupo", source = "actividadGrupo.idActividadGrupo")
    ActividadAlumnoDto toDto(ActividadAlumno actividadAlumno);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ActividadAlumno partialUpdate(ActividadAlumnoDto actividadAlumnoDto, @MappingTarget ActividadAlumno actividadAlumno);
}
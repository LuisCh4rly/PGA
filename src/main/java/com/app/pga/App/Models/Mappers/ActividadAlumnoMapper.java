package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.ActividadAlumnoDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadAlumnoResponseDTO;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadBaseResponseDto;
import com.app.pga.App.Models.Entities.ActividadAlumno;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActividadAlumnoMapper {

    ActividadAlumno toEntity(ActividadAlumnoDto actividadAlumnoDto);

    @Mapping(target = "idInscripcion", source = "inscripcion.idInscripcion")
    @Mapping(target = "idActividadGrupo", source = "actividadGrupo.idActividadGrupo")
    ActividadAlumnoDto toDto(ActividadAlumno actividadAlumno);


    @Mapping(target = "idInscripcion", source = "inscripcion.idInscripcion")
    @Mapping(target = "idActividadGrupo", source = "actividadGrupo.idActividadGrupo")
    @Mapping(target = "nombre", expression = "java( actividadAlumno.getInscripcion().getUsuario().getNombre() + \" \" + " +
            "actividadAlumno.getInscripcion().getUsuario().getApellidoPaterno() + \" \" + " +
            "actividadAlumno.getInscripcion().getUsuario().getApellidoMaterno())"
    )
    ActividadAlumnoResponseDTO toDtoResponse (ActividadAlumno actividadAlumno);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ActividadAlumno partialUpdate(ActividadAlumnoDto actividadAlumnoDto, @MappingTarget ActividadAlumno actividadAlumno);
}
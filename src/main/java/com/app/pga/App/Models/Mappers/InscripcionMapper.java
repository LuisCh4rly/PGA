package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.InscripcionDto;
import com.app.pga.App.Models.Entities.Inscripcion;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {AlumnoMapper.class,GrupoMapper.class})
public interface InscripcionMapper {

    @Mapping(source = "alumno", target = "alumno")
    Inscripcion toEntity(InscripcionDto inscripcionDto);

    @Mapping(source = "alumno", target = "alumno")
    InscripcionDto toDto(Inscripcion inscripcion);

    @Named("ResumenInscripcion")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "alumno", target = "alumno", qualifiedByName = "ResumenAlumno")
    @Mapping(source = "grupo", target = "grupo", qualifiedByName = "ResumenGrupo")
    InscripcionDto toDtoResumen(Inscripcion inscripcion);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "alumno", target = "alumno")
    Inscripcion partialUpdate(InscripcionDto inscripcionDto, @MappingTarget Inscripcion inscripcion);
}
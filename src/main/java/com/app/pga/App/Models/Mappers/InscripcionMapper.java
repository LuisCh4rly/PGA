package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.InscripcionDto;
import com.app.pga.App.Models.Entities.Inscripcion;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = AlumnoMapper.class)
public interface InscripcionMapper {

    @Mapping(source = "alumno", target = "alumno")
    Inscripcion toEntity(InscripcionDto inscripcionDto);

    @Mapping(source = "alumno", target = "alumno")
    InscripcionDto toDto(Inscripcion inscripcion);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "alumno", target = "alumno")
    Inscripcion partialUpdate(InscripcionDto inscripcionDto, @MappingTarget Inscripcion inscripcion);
}
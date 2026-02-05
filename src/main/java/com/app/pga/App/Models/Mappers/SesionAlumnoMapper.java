package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.ResponseDto.SesionAlumnoDto;
import com.app.pga.App.Models.Entities.SesionAlumno;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SesionAlumnoMapper {
    SesionAlumno toEntity(SesionAlumnoDto sesionAlumnoDto);

    SesionAlumnoDto toDto(SesionAlumno sesionAlumno);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SesionAlumno partialUpdate(SesionAlumnoDto sesionAlumnoDto, @MappingTarget SesionAlumno sesionAlumno);
}
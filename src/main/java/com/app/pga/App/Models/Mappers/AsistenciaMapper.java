package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.AsistenciaDto;
import com.app.pga.App.Models.Entities.Asistencia;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AsistenciaMapper {
    Asistencia toEntity(AsistenciaDto asistenciaDto);

    AsistenciaDto toDto(Asistencia asistencia);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Asistencia partialUpdate(AsistenciaDto asistenciaDto, @MappingTarget Asistencia asistencia);
}
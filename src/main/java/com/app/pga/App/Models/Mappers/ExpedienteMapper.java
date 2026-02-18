package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.ResponseDto.ExpedienteResponseDto;
import com.app.pga.App.Models.Entities.Expediente;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExpedienteMapper {
    Expediente toEntity(ExpedienteResponseDto expedienteResponseDto);

    @Mapping(target = "idAlumno" , source = "usuario.idUsuario")
    ExpedienteResponseDto toDto(Expediente expediente);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Expediente partialUpdate(ExpedienteResponseDto expedienteResponseDto, @MappingTarget Expediente expediente);
}
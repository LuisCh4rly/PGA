package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.ActividadGrupoDto;
import com.app.pga.App.Models.Entities.ActividadGrupo;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActividadGrupoMapper {
    ActividadGrupo toEntity(ActividadGrupoDto actividadGrupoDto);

    ActividadGrupoDto toDto(ActividadGrupo actividadGrupo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ActividadGrupo partialUpdate(ActividadGrupoDto actividadGrupoDto, @MappingTarget ActividadGrupo actividadGrupo);
}
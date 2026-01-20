package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.ActividadBaseDto;
import com.app.pga.App.Models.Entities.ActividadBase;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActividadBaseMapper {
    ActividadBase toEntity(ActividadBaseDto actividadBaseDto);

    ActividadBaseDto toDto(ActividadBase actividadBase);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ActividadBase partialUpdate(ActividadBaseDto actividadBaseDto, @MappingTarget ActividadBase actividadBase);
}
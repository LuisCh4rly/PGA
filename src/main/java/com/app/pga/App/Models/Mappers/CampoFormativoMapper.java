package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.CampoFormativoDto;
import com.app.pga.App.Models.Entities.CampoFormativo;
import org.mapstruct.*;

@Mapper( unmappedTargetPolicy = ReportingPolicy.IGNORE,componentModel = MappingConstants.ComponentModel.SPRING)
public interface CampoFormativoMapper {

    CampoFormativo toEntity(CampoFormativoDto campoFormativoDto);

    CampoFormativoDto toDto(CampoFormativo campoFormativo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    //si un dto trae un campo null no lo copies sobre la entidad
    CampoFormativo partialUpdate(CampoFormativoDto campoFormativoDto, @MappingTarget CampoFormativo campoFormativo);
}
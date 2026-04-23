package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.RequestDto.ActividadBaseRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadBaseResponseDto;
import com.app.pga.App.Models.Entities.ActividadBase;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = CampoFormativoMapper.class)
public interface ActividadBaseMapper {
    ActividadBase toEntity(ActividadBaseRequestDto actividadBaseRequestDto);
    @Mapping(target= "campoFormativo", source = "campoFormativo", qualifiedByName = "toDto")
    ActividadBaseResponseDto toDto(ActividadBase actividadBase);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ActividadBase partialUpdate(ActividadBaseRequestDto actividadBaseDto, @MappingTarget ActividadBase actividadBase);
}
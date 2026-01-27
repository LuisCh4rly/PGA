package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.GrupoDto;
import com.app.pga.App.Models.Entities.Grupo;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = CursoMapper.class )
public interface GrupoMapper {
    @Mapping(target = "docente", ignore = true)
    @Mapping(target =  "curso", ignore = true)
    Grupo toEntity(GrupoDto grupoDto);

    @Mapping(target = "curso", source = "curso", qualifiedByName = "simple")
    GrupoDto toDtoSimple(Grupo grupo);


    @Mapping(target = "curso", source = "curso", qualifiedByName = "conActividades")
    GrupoDto toDtoActividades(Grupo grupo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Grupo partialUpdate(GrupoDto grupoDto, @MappingTarget Grupo grupo);
}
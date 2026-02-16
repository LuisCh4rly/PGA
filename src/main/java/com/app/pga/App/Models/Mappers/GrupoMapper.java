package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.GrupoDto;
import com.app.pga.App.Models.Entities.Grupo;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = CursoMapper.class )
public interface GrupoMapper {
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target =  "curso", ignore = true)
    Grupo toEntity(GrupoDto grupoDto);

    @Named("GrupoSimple")
    @Mapping(target = "curso", source = "curso", qualifiedByName = "simple")
    GrupoDto toDtoSimple(Grupo grupo);

    @Named("GrupoconActividades")
    @Mapping(target = "curso", source = "curso", qualifiedByName = "conActividades")
    GrupoDto toDtoActividades(Grupo grupo);

    @Named("ResumenGrupo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "idGrupo", source = "idGrupo")
    @Mapping(target = "nombre", source = "nombre")
    GrupoDto toDtoResumen(Grupo grupo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Grupo partialUpdate(GrupoDto grupoDto, @MappingTarget Grupo grupo);
}
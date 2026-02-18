package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.RequestDto.GrupoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.GrupoResponseDto;
import com.app.pga.App.Models.Entities.Grupo;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface GrupoMapper {

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target =  "curso", ignore = true)
    Grupo toEntity(GrupoRequestDto grupoDto);

    @Named("GrupoSimple")
    @Mapping(target = "idCurso", source = "curso.idCurso")
    @Mapping(target = "nombreCurso", source = "curso.nombre")
    @Mapping(target = "docente",  expression = "java(grupo.getUsuario().getNombre() + \" \" + " +
            "grupo.getUsuario().getApellidoPaterno() + \" \" + " +
            "grupo.getUsuario().getApellidoMaterno())"
    )
    GrupoResponseDto toDto(Grupo grupo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Grupo partialUpdate(GrupoRequestDto grupoRequestDto, @MappingTarget Grupo grupo);
}
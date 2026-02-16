package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.InscripcionDto;
import com.app.pga.App.Models.Entities.Inscripcion;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UsuarioMapper.class,GrupoMapper.class})
public interface InscripcionMapper {

    @Mapping(source = "usuario", target = "usuario")
    Inscripcion toEntity(InscripcionDto inscripcionDto);

    @Mapping(source = "usuario", target = "usuario")
    InscripcionDto toDto(Inscripcion inscripcion);

    @Named("ResumenInscripcion")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "usuario", target = "usuario", qualifiedByName = "ResumenUsuario")
    @Mapping(source = "grupo", target = "grupo", qualifiedByName = "ResumenGrupo")
    InscripcionDto toDtoResumen(Inscripcion inscripcion);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "usuario", target = "usuario")
    Inscripcion partialUpdate(InscripcionDto inscripcionDto, @MappingTarget Inscripcion inscripcion);
}
package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.AlumnoDto;
import com.app.pga.App.Models.Dtos.GrupoDto;
import com.app.pga.App.Models.Entities.Alumno;
import com.app.pga.App.Models.Entities.Grupo;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = UsuarioMapper.class)
public interface AlumnoMapper {

    @Mapping(source = "usuarioDto", target = "usuario")
    Alumno toEntity(AlumnoDto alumnoDto);

    @Mapping(source = "usuario", target = "usuarioDto")
    AlumnoDto toDto(Alumno alumno);

    @Named("ResumenAlumno")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "idAlumno", source = "idAlumno")
    @Mapping(target = "usuarioDto", source = "usuario", qualifiedByName = "ResumenUsuario")
    AlumnoDto toDtoResumen(Alumno alumno);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "usuarioDto", target = "usuario")
    Alumno partialUpdate(AlumnoDto alumnoDto, @MappingTarget Alumno alumno);
}
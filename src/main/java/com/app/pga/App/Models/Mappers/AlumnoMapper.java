package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.AlumnoDto;
import com.app.pga.App.Models.Entities.Alumno;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = UsuarioMapper.class)
public interface AlumnoMapper {

    @Mapping(source = "usuarioDto", target = "usuario")
    Alumno toEntity(AlumnoDto alumnoDto);

    @Mapping(source = "usuario", target = "usuarioDto")
    AlumnoDto toDto(Alumno alumno);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "usuarioDto", target = "usuario")
    Alumno partialUpdate(AlumnoDto alumnoDto, @MappingTarget Alumno alumno);
}
package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.RequestDto.AlumnoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.AlumnoResponseDto;
import com.app.pga.App.Models.Entities.Alumno;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = UsuarioMapper.class)
public interface AlumnoMapper {

    Alumno toEntity(AlumnoRequestDto alumnoDto);
    @Named("ResumenAlumno")
    @Mapping(target = "nombre", source = "usuario.nombre")
    @Mapping(target = "apellidoPaterno", source = "usuario.apellidoPaterno")
    @Mapping(target = "apellidoMaterno", source = "usuario.apellidoMaterno")
    AlumnoResponseDto toDto(Alumno alumno);



    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    Alumno partialUpdate(AlumnoRequestDto alumnoDto, @MappingTarget Alumno alumno);
}
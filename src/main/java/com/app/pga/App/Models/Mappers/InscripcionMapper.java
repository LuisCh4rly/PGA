package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.RequestDto.InscripcionRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.InscripcionResponseDto;
import com.app.pga.App.Models.Entities.Inscripcion;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InscripcionMapper {


    Inscripcion toEntity(InscripcionRequestDto inscripcionDto);

    @Mapping(
            target = "alumno",
            expression = "java(inscripcion.getUsuario().getNombre() + \" \" + " +
                    "inscripcion.getUsuario().getApellidoPaterno() + \" \" + " +
                    "inscripcion.getUsuario().getApellidoMaterno())"
    )
    @Mapping(target = "idGrupo", source = "grupo.idGrupo")
    @Mapping(target = "nombreGrupo", source = "grupo.nombre")
    InscripcionResponseDto toDto(Inscripcion inscripcion);





    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Inscripcion partialUpdate(InscripcionRequestDto inscripcionDto, @MappingTarget Inscripcion inscripcion);
}
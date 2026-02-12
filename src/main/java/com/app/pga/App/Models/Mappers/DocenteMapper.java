package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.RequestDto.DocenteRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DocenteResponseDto;
import com.app.pga.App.Models.Entities.Docente;
import org.mapstruct.*;

@Mapper( unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = UsuarioMapper.class)
public interface DocenteMapper {

    Docente toEntity(DocenteRequestDto docenteRequestDto);

    @Mapping(target = "nombre",source = "usuario.nombre")
    @Mapping(target = "apellidoPaterno",source = "usuario.apellidoPaterno")
    @Mapping(target = "apellidoMaterno",source = "usuario.apellidoMaterno")
    DocenteResponseDto toDto(Docente docente);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "idUsuario", target = "usuario.idUsuario")
    Docente partialUpdate(DocenteRequestDto docenteDto, @MappingTarget Docente docente);
}
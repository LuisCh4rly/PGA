package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.DocenteDto;
import com.app.pga.App.Models.Entities.Docente;
import org.mapstruct.*;

@Mapper( unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = UsuarioMapper.class)
public interface DocenteMapper {

    @Mapping(source = "usuarioDto", target = "usuario")
    Docente toEntity(DocenteDto docenteDto);

    @Mapping(source = "usuario", target = "usuarioDto")
    DocenteDto toDto(Docente docente);

    //@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    //@Mapping(source = "idUser", target = "usuario.idUser")
    //Docente partialUpdate(DocenteDto docenteDto, @MappingTarget Docente docente);
}
package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.DocenteDto;
import com.app.pga.App.Models.Entities.Docente;
import org.mapstruct.*;

@Mapper( unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DocenteMapper {

    @Mapping(source = "idUser", target = "usuario.idUser")
    Docente toEntity(DocenteDto docenteDto);

    @Mapping(source = "usuario.idUser", target = "idUser")
    DocenteDto toDto(Docente docente);

    //@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    //@Mapping(source = "idUser", target = "usuario.idUser")
    //Docente partialUpdate(DocenteDto docenteDto, @MappingTarget Docente docente);
}
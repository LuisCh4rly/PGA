package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.AlumnoDto;
import com.app.pga.App.Models.Dtos.UsuarioDto;
import com.app.pga.App.Models.Entities.Alumno;
import com.app.pga.App.Models.Entities.Usuario;
import org.mapstruct.*;
//politica para saber que hacer en caso de que las etiquestas no coincidan, se comporte como componente(inyeccion)
//unmappedTargetPolicy = ReportingPolicy.IGNORE,
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDto usuarioDto);

    UsuarioDto toDto(Usuario usuario);

    @Named("ResumenUsuario")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "apellidoPaterno", source = "apellidoPaterno")
    @Mapping(target = "apellidoMaterno", source = "apellidoMaterno")
    UsuarioDto toDtoResumen(Usuario usuario);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)//los nulos se ignoran
    Usuario partialUpdate(UsuarioDto usuarioDto, @MappingTarget Usuario usuario); //actualización parcial de la entidad
}
package com.app.pga.App.Models.Mappers;


import com.app.pga.App.Models.Dtos.RequestDto.UsuarioRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.UsuarioResponseDto;
import com.app.pga.App.Models.Entities.Usuario;
import org.mapstruct.*;
//politica para saber que hacer en caso de que las etiquestas no coincidan, se comporte como componente(inyeccion)
//unmappedTargetPolicy = ReportingPolicy.IGNORE,
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper {

    Usuario toEntity(UsuarioRequestDto usuarioDto);

    @Named("ResumenUsuario")
    UsuarioResponseDto toDto(Usuario usuario);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)//los nulos se ignoran
    Usuario partialUpdate(UsuarioRequestDto usuarioRequestDto, @MappingTarget Usuario usuario); //actualización parcial de la entidad
}
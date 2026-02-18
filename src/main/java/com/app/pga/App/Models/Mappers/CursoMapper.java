package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.RequestDto.CursoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadBaseResponseDto;
import com.app.pga.App.Models.Dtos.ResponseDto.CursoResponseDto;
import com.app.pga.App.Models.Entities.ActividadBase;
import com.app.pga.App.Models.Entities.Curso;
import com.app.pga.App.Models.Entities.Curso_ActividadBase;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING, uses = CampoFormativoMapper.class)
public interface CursoMapper {

    @Mapping(target = "actividades",
            ignore = true)
    Curso toEntity(CursoRequestDto cursoDto);
    @Named("conActividades")
    @Mapping(
            target = "actividades",
            source = "actividades"
    )
    CursoResponseDto toDtoConActividades(Curso curso);
    default List<ActividadBaseResponseDto> map(List<Curso_ActividadBase> relaciones) {
        if (relaciones == null) {
            return List.of();
        }
        return relaciones.stream()
                .map(Curso_ActividadBase::getActividadBase)
                .map(this::toDto)
                .toList();
    }
    @Mapping(target= "campoFormativo", source = "campoFormativo", qualifiedByName = "toDto")
    ActividadBaseResponseDto toDto(ActividadBase entity);

    @Named("simple")
    @Mapping(
            target = "actividades",
            ignore = true)
    CursoResponseDto toDtoSimple(Curso curso);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Curso partialUpdate(CursoRequestDto cursoDto, @MappingTarget Curso curso);
}
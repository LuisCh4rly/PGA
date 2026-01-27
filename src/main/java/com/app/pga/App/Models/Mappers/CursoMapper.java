package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.ActividadBaseDto;
import com.app.pga.App.Models.Dtos.CursoDto;
import com.app.pga.App.Models.Entities.ActividadBase;
import com.app.pga.App.Models.Entities.Curso;
import com.app.pga.App.Models.Entities.Curso_ActividadBase;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface CursoMapper {

    @Mapping(target = "actividades",
            ignore = true)
    Curso toEntity(CursoDto cursoDto);
    @Named("conActividades")
    @Mapping(
            target = "actividades",
            source = "actividades"
    )
    CursoDto toDtoConActividades(Curso curso);
    default List<ActividadBaseDto> map( List<Curso_ActividadBase> relaciones) {
        if (relaciones == null) {
            return List.of();
        }
        return relaciones.stream()
                .map(Curso_ActividadBase::getActividadBase)
                .map(this::toDto)
                .toList();
    }
    ActividadBaseDto toDto(ActividadBase entity);

    @Named("simple")
    @Mapping(
            target = "actividades",
            ignore = true)
    CursoDto toDtoSimple(Curso curso);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Curso partialUpdate(CursoDto cursoDto, @MappingTarget Curso curso);
}
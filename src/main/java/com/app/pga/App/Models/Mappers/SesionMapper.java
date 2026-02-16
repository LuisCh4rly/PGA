package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.RequestDto.SesionRequestDto;
import com.app.pga.App.Models.Dtos.RequestDto.SesionUpdateDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SesionAlumnoDetalleDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SesionDetalletDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SesionDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SesiondocenteDto;
import com.app.pga.App.Models.Entities.Sesion;
import com.app.pga.App.Models.Entities.SesionAlumno;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SesionMapper {

    Sesion toEntity(SesionDto sesionDto);

    //CREATE
    @Mapping(target = "idSesion", ignore = true)
    @Mapping(target = "sesionAlumnos", ignore = true)
    @Mapping(target = "grupo", ignore = true)
    Sesion toEntiity (SesionRequestDto dto);

    //Update
    @Mapping(target = "idSesion", ignore = true)
    @Mapping(target = "sesionAlumnos", ignore = true)
    @Mapping(target = "grupo", ignore = true)
    void updateEntityFromDto(SesionUpdateDto dto, @MappingTarget Sesion entity);

    //Detalle
    @Mapping(source = "sesionAlumnos", target = "alumnos")
    SesionDetalletDto toDetalleDto(Sesion sesion);

    //Response
    @Mapping(source = "grupo", target = "grupo")
    SesionDto toDto(Sesion sesion);
    List<SesionDto>toDtoList(List<Sesion> sesiones);

    //Hijos
    @Mapping(source = "inscripcion.idInscripcion", target = "idInscripcion")
    @Mapping(source = "inscripcion.usuario.nombre", target = "nombreAlumno")
    @Mapping(source = "inscripcion.usuario.apellidoPaterno", target = "apellidoPaterno")
    @Mapping(source = "inscripcion.usuario.apellidoMaterno", target = "apellidoMaterno")
    @Mapping(source = "asistencia.estado", target = "estadoAsistencia")
    SesionAlumnoDetalleDto toAlumnoDetalleDto(SesionAlumno sesionAlumno);



    @Mapping(source = "grupo.nombre", target = "nombreGrupo")
    SesiondocenteDto toDocenteDto(Sesion sesion);

}
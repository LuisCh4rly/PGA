package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.RequestDto.AgregarActividadesCursoDto;
import com.app.pga.App.Models.Dtos.RequestDto.CursoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.CursoResponseDto;

import java.util.List;

public interface ICursoService {

    CursoResponseDto crearCurso (CursoRequestDto cursoRequestDto);
    CursoResponseDto obtenerCurso (Long idActividadBase);
    List<CursoResponseDto> obtenerCursosGeneral ();
    List<CursoResponseDto> obtenerCursosActivos ();
    CursoResponseDto actualizarCurso( CursoRequestDto cursoRequestDto, Long idActividadBase);
    CursoResponseDto habitarDeshabilitar(Long idActividadBase);
    CursoResponseDto asignarActividades (Long idCurso, AgregarActividadesCursoDto agregarActividadesCursoDto);
    void quitarActividades (Long idCurso, Long idActividad);

}

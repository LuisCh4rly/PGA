package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.ActividadBaseDto;
import com.app.pga.App.Models.Dtos.CursoDto;

import java.util.List;

public interface ICursoService {

    CursoDto crearCurso (CursoDto cursoDtoto);
    CursoDto obtenerCurso (Long idActividadBase);
    List<CursoDto> obtenerCursosGeneral ();
    List<CursoDto> obtenerCursosActivos ();
    CursoDto actualizarCurso( CursoDto actividadBaseDto, Long idActividadBase);
    CursoDto habitarDeshabilitar(Long idActividadBase);
    CursoDto asignarActividades (Long idCurso, List<ActividadBaseDto> actividades);
    void quitarActividades (Long idCurso, Long idActividad);

}

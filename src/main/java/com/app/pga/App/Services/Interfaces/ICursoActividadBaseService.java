package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.ActividadBaseDto;
import com.app.pga.App.Models.Entities.ActividadBase;
import com.app.pga.App.Models.Entities.Curso;

import java.util.List;

public interface ICursoActividadBaseService {
    void asignarActividadABCurso(Curso curso, List<ActividadBaseDto> actividades);
    void quitarActividadDeCurso(Long cursoId, Long actividadId);
    List<ActividadBase> obtenerActividadesDeCurso(Long cursoId);
}

package com.app.pga.App.Services.Implements;

import com.app.pga.App.Models.Dtos.ResponseDto.ActividadBaseResponseDto;
import com.app.pga.App.Models.Entities.ActividadBase;
import com.app.pga.App.Models.Entities.Curso;
import com.app.pga.App.Models.Entities.Curso_ActividadBase;
import com.app.pga.App.Repositories.IActividadBaseRepository;
import com.app.pga.App.Repositories.ICursoRepository;
import com.app.pga.App.Repositories.ICurso_ActividadBaseRepository;
import com.app.pga.App.Services.Interfaces.ICursoActividadBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
class CursoActividadBaseService implements ICursoActividadBaseService {
    private final ICursoRepository cursoRepository;
    private final IActividadBaseRepository actividadBaseRepository;
    private final ICurso_ActividadBaseRepository cursoActividadBaseRepository;


    @Override
    public void asignarActividadABCurso(Curso curso, List<ActividadBaseResponseDto> actividades) {


        for (ActividadBaseResponseDto a : actividades) {
            ActividadBase ab = actividadBaseRepository.findById(a.idActividad())
                    .orElseThrow(() -> new RuntimeException("Actividad base no encontrada"));

            if(!ab.getActivo()){
                throw new IllegalArgumentException ("Actividad: "+ ab.getTitulo()+ " está deshabilitada");
            }
            if (cursoActividadBaseRepository.existsByCurso_IdCursoAndActividadBase_IdActividad(curso.getIdCurso(), ab.getIdActividad())){
                throw new IllegalStateException("La actividad ya está asignada al curso");
            }

            Curso_ActividadBase cab = Curso_ActividadBase.builder()
                    .curso(curso)
                    .actividadBase(ab)
                    .build();

            cursoActividadBaseRepository.save(cab);
        }
    }

    @Override
    public void quitarActividadDeCurso(Long cursoId, Long actividadId) {

    }

    @Override
    public List<ActividadBase> obtenerActividadesDeCurso(Long cursoId) {
        return List.of();
    }
}

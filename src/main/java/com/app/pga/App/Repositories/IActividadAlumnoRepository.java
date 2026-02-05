package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.ActividadAlumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IActividadAlumnoRepository extends JpaRepository<ActividadAlumno, Long> {
    List<ActividadAlumno> findByActividadGrupo_IdActividadGrupo(Long actividadGrupoIdActividadGrupo);

    List<ActividadAlumno> findByInscripcion_IdInscripcion(Long inscripcionIdInscripcion);
    boolean existsByActividadGrupo_IdActividadGrupoAndInscripcion_IdInscripcion(Long idActividadGrupo, Long idInscripcion
    );

}
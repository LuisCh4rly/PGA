package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Curso_ActividadBase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICurso_ActividadBaseRepository extends JpaRepository<Curso_ActividadBase, Long> {
   boolean existsByCurso_IdCursoAndActividadBase_IdActividad(Long cursoId, Long actividadId);
   Optional<Curso_ActividadBase> findByCurso_IdCursoAndActividadBase_IdActividad(Long cursoIdCurso, Long actividadBaseIdActividad);
}
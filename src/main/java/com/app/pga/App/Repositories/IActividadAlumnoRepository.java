package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Dtos.ReporteSeguimientoDto;
import com.app.pga.App.Models.Entities.ActividadAlumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IActividadAlumnoRepository extends JpaRepository<ActividadAlumno, Long> {
    List<ActividadAlumno> findByActividadGrupo_IdActividadGrupo(Long actividadGrupoIdActividadGrupo);

    List<ActividadAlumno> findByInscripcion_IdInscripcion(Long inscripcionIdInscripcion);
    boolean existsByActividadGrupo_IdActividadGrupoAndInscripcion_IdInscripcion(Long idActividadGrupo, Long idInscripcion
    );

    @Query("""
    SELECT new com.app.pga.App.Models.Dtos.ReporteSeguimientoDto(
        g.idGrupo,
        g.nombre,
        ag.idActividadGrupo,
        ag.titulo,
        ag.reqEntrega,
        ag.alcance,
        au.idActividadAlumno,
        COALESCE(au.estadoTarea, com.app.pga.App.Models.Enum.EstadoTarea.Aprobada),
        au.excento,
        au.fechaEntrega,
        u.idUsuario,
        CONCAT(u.nombre, ' ', u.apellidoPaterno, ' ', u.apellidoMaterno)
    )
    FROM ActividadAlumno au
    JOIN au.actividadGrupo ag
    JOIN ag.grupo g
    JOIN au.inscripcion i
    JOIN i.usuario u
    ORDER BY g.idGrupo, ag.idActividadGrupo, u.nombre
""")
    List<ReporteSeguimientoDto> reporteSeguimientoGeneral();

}
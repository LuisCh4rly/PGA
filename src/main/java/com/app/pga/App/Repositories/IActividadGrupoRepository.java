package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Dtos.ActividadGrupoDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadGrupoDashboardDto;
import com.app.pga.App.Models.Dtos.ResponseDto.GrupoResumenDto;
import com.app.pga.App.Models.Entities.ActividadGrupo;
import com.app.pga.App.Models.Enum.Alcance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IActividadGrupoRepository extends JpaRepository<ActividadGrupo, Long> {
    List<ActividadGrupo> findByGrupo_IdGrupo(Long idGrupo);
    List<ActividadGrupo> findByGrupo_IdGrupoAndAlcance(Long idGrupo, Alcance alcance);


    @Query("""
    SELECT NEW com.app.pga.App.Models.Dtos.ResponseDto.ActividadGrupoDashboardDto(
        ag.idActividadGrupo,
        ag.titulo,
        ag.descripcion,
        ag.fechaAsignacion,
        ag.reqEntrega,
        ag.alcance,
        ag.grupo.idGrupo,
        ag.grupo.nombre,
        ag.campo,
        ag.origen,
        ag.urlInstrucciones,

        CAST( (SELECT COUNT(aa)
        FROM ActividadAlumno aa
        WHERE aa.actividadGrupo = ag) AS Long),

        CAST( (SELECT COUNT(aa)
        FROM ActividadAlumno aa
        WHERE aa.actividadGrupo = ag
        AND aa.estadoTarea = com.app.pga.App.Models.Enum.EstadoTarea.Completada)AS Long),

        CAST( (SELECT COUNT(aa)
         FROM ActividadAlumno aa
        WHERE aa.actividadGrupo = ag\s
        AND aa.estadoTarea = com.app.pga.App.Models.Enum.EstadoTarea.Aprobada)AS Long),

        CAST( (SELECT COUNT(aa)
        FROM ActividadAlumno aa
        WHERE aa.actividadGrupo = ag
        AND aa.estadoTarea = com.app.pga.App.Models.Enum.EstadoTarea.Incompleta)AS Long),

        CAST( (SELECT COUNT(aa)
        FROM ActividadAlumno aa
        WHERE aa.actividadGrupo = ag
        AND aa.estadoTarea = com.app.pga.App.Models.Enum.EstadoTarea.Exenta)AS Long),
        CAST( (SELECT COUNT(aa)
        FROM ActividadAlumno aa
        WHERE aa.actividadGrupo = ag
        AND aa.estadoTarea = com.app.pga.App.Models.Enum.EstadoTarea.En_Progreso)AS Long),
        CAST( (SELECT COUNT(aa)
        FROM ActividadAlumno aa
        WHERE aa.actividadGrupo = ag
        AND aa.estadoTarea = com.app.pga.App.Models.Enum.EstadoTarea.En_Espera)AS Long),
        CAST( (SELECT COUNT(aa)
        FROM ActividadAlumno aa
        WHERE aa.actividadGrupo = ag
        AND aa.estadoTarea = com.app.pga.App.Models.Enum.EstadoTarea.Sin_Iniciar)AS Long)
            
        )
        FROM ActividadGrupo ag
        WHERE ag.idActividadGrupo = :idActividad
        """)
   Optional< ActividadGrupoDashboardDto> obtenerPorId (Long idActividad);

    @Query("""
     SELECT NEW com.app.pga.App.Models.Dtos.ResponseDto.GrupoResumenDto(
           g.nombre,
           g.idGrupo
       )
       FROM Grupo g
       WHERE g.usuario.idUsuario = :idDocente
         AND NOT EXISTS (
           SELECT 1 FROM ActividadGrupo ag
           WHERE ag.grupo.idGrupo = g.idGrupo
             AND LOWER(TRIM(ag.titulo)) = LOWER(TRIM(:titulo))
         )
    """)
    List<GrupoResumenDto> obtenerGruposSinActividad(String titulo, Long idDocente);
    @Query("""
    SELECT NEW com.app.pga.App.Models.Dtos.ActividadGrupoDto (
        ag.idActividadGrupo,
        ag.titulo,
        ag.descripcion,
        ag.fechaAsignacion,
        ag.reqEntrega,
        ag.alcance,
        ag.grupo.idGrupo,
        ag.grupo.nombre,
        ag.campo,
        ag.origen    
        )
    FROM ActividadGrupo  ag
    WHERE ag.grupo.usuario.idUsuario = :idDocente
    """)
    List<ActividadGrupoDto> obtenerActividadesporGrupos(Long idDocente);
}
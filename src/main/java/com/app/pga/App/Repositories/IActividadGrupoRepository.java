package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Dtos.ActividadGrupoDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadGrupoDashboardDto;
import com.app.pga.App.Models.Entities.ActividadGrupo;
import com.app.pga.App.Models.Enum.Alcance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

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
        AND aa.estadoTarea = com.app.pga.App.Models.Enum.EstadoTarea.Exenta)AS Long)
        )
        FROM ActividadGrupo ag
        WHERE ag.idActividadGrupo = :idActividad
        """)
   Optional< ActividadGrupoDashboardDto> obtenerPorId (Long idActividad);

}
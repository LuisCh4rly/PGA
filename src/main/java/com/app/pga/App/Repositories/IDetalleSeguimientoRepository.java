package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Dtos.ActividadAlumnoDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DetalleDashboardDto;
import com.app.pga.App.Models.Entities.ActividadAlumno;
import com.app.pga.App.Models.Entities.DetalleSeguimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface IDetalleSeguimientoRepository extends JpaRepository<DetalleSeguimiento, Long>, JpaSpecificationExecutor<DetalleSeguimiento> {
    @Query("""
SELECT DISTINCT d.actividad
FROM DetalleSeguimiento d
WHERE d.semana.idSeguimientoSemanal = :idSemana
AND d.actividad.inscripcion.idInscripcion = :idInscripcion
AND d.actividad.avanceGlobal < 100
""")
    List<ActividadAlumno> actividadesPendientes(Long idSemana, Long idInscripcion );
    Boolean existsBySemana_IdSeguimientoSemanalAndActividad_IdActividadAlumno(Long idSeguimientoSemanal, Long idActividad);

    @Query("""
SELECT DISTINCT d
FROM DetalleSeguimiento d
WHERE d.semana.idSeguimientoSemanal = :idSemana
AND d.actividad.inscripcion.idInscripcion = :idInscripcion
AND d.actividad.avanceGlobal < 100
""")
    List<DetalleSeguimiento> pendientes(Long idSemana,  Long idInscripcion);


    @Query("""
        SELECT NEW com.app.pga.App.Models.Dtos.ResponseDto.DetalleDashboardDto(
        d.idDetalleSeguimiento,
        d.estadoSemana,
        d.avanceReal,
        d.observacionesAlumno,
        a.actividadGrupo.titulo,
        a.avanceGlobal,
        a.idActividadAlumno,
        a.actividadGrupo.reqEntrega,
        d.semana.idSeguimientoSemanal,
        d.avanceEsperado
        )
        FROM DetalleSeguimiento d
        JOIN d.actividad a
        JOIN d.semana s
        WHERE s.idSeguimientoSemanal IN :idsSemana
    """)
    List<DetalleDashboardDto> obtenerDetalles (List<Long> idsSemana);

    @Query("""
        SELECT d
        FROM DetalleSeguimiento d
        WHERE d.actividad.idActividadAlumno = :idActividadAlumno
        AND CURRENT_DATE <= d.semana.fechaLimiteEdicion
       """)
    Optional<DetalleSeguimiento> findDetalleActivo(Long idActividadAlumno);

    boolean existsByActividad_IdActividadAlumnoAndSemana_FechaLimiteEdicionGreaterThanEqual( Long idActividadAlumno, LocalDate fecha );

    List<DetalleSeguimiento> findBySemana_IdSeguimientoSemanal(Long idSeguimientoSemanal);



}



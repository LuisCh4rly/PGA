package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Dtos.ReporteAsistenciaGrupoDto;
import com.app.pga.App.Models.Entities.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ISesionRepository extends JpaRepository<Sesion, Long> {

    Optional<Sesion> findSesionByFecha(LocalDateTime fecha);
    Boolean existsByGrupo_Docente_IdDocenteAndFecha(Long idDocente, LocalDateTime fecha);
    List<Sesion> findByGrupo_IdGrupo(Long idGrupo);
    List<Sesion> findByGrupo_Docente_IdDocente(Long idDocente);

    @Query("""
    SELECT new com.app.pga.App.Models.Dtos.ReporteAsistenciaGrupoDto(
        g.idGrupo,
        g.nombre,
        s.idSesion,
        s.fecha,
        s.tema,
        s.alcance,
        a.idAlumno,
        CONCAT(u.nombre, ' ', u.apellidoPaterno, ' ', u.apellidoMaterno),
        COALESCE(asist.estado, com.app.pga.App.Models.Enum.EstadoAsistencia.FALTO)
    )
    FROM Sesion s
    JOIN s.grupo g
    JOIN s.sesionAlumnos sa
    JOIN sa.inscripcion i
    JOIN i.alumno a
    JOIN a.usuario u
    LEFT JOIN sa.asistencia asist
    ORDER BY g.idGrupo, s.fecha, u.nombre
""")
    List<ReporteAsistenciaGrupoDto> reporteAsistenciaGeneral();
}
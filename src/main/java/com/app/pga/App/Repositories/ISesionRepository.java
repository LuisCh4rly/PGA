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

    @Query("""
    SELECT COUNT(s) > 0
    FROM Sesion s
    WHERE s.grupo.usuario.idUsuario = :idUsuario
    AND s.grupo.usuario.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.DOCENTE
    AND s.fecha = :fecha
""")
    Boolean existsSesionActivaDocente(Long idUsuario, LocalDateTime fecha);

    Boolean existsByGrupo_Usuario_IdUsuarioAndFecha(Long idUsuario, LocalDateTime fecha);
    List<Sesion> findByGrupo_IdGrupo(Long idGrupo);
    @Query("""
    SELECT s
    FROM Sesion s
    WHERE s.grupo.usuario.idUsuario = :idUsuario
    AND s.grupo.usuario.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.DOCENTE
""")
    List<Sesion> findSesionesByDocente(Long idUsuario);

 
    @Query("""
    SELECT new com.app.pga.App.Models.Dtos.ReporteAsistenciaGrupoDto(
        g.idGrupo,
        g.nombre,
        s.idSesion,
        s.fecha,
        s.tema,
        s.alcance,
        u.idUsuario,
        CONCAT(u.nombre, ' ', u.apellidoPaterno, ' ', u.apellidoMaterno),
        COALESCE(asist.estado, com.app.pga.App.Models.Enum.EstadoAsistencia.FALTO)
    )
    FROM Sesion s
    JOIN s.grupo g
    JOIN s.sesionAlumnos sa
    JOIN sa.inscripcion i
    JOIN i.usuario u
    LEFT JOIN sa.asistencia asist
    ORDER BY g.idGrupo, s.fecha, u.nombre
""")
    List<ReporteAsistenciaGrupoDto> reporteAsistenciaGeneral();
}
package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Dtos.ResponseDto.GrupoResponseDashboardDto;
import com.app.pga.App.Models.Entities.Curso;
import com.app.pga.App.Models.Entities.Grupo;
import com.app.pga.App.Models.Enum.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IGrupoRepository extends JpaRepository<Grupo, Long>, JpaSpecificationExecutor {
    boolean existsByNombreEqualsIgnoreCase(String nombre);
    List<Grupo> findByCursoIdCurso(Long idCurso);

    @Query("""
    SELECT new com.app.pga.App.Models.Dtos.ResponseDto.GrupoResponseDashboardDto(
    g.idGrupo,
    g.nombre,
    c.nombre,
    count(i),
    g.estado)
    FROM Grupo g
    LEFT JOIN g.curso c
    LEFT JOIN Inscripcion i ON i.grupo.idGrupo = g.idGrupo
    WHERE g.usuario.idUsuario = :idUsuario
    AND g.usuario.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.DOCENTE
    GROUP BY g.idGrupo, g.nombre, c.nombre, g.estado
""")
    List<GrupoResponseDashboardDto> findGruposByDocente(@Param("idUsuario") Long idUsuario);

    Optional<Grupo> findByIdGrupo(Long idGrupo);

    List<Grupo> findByEstado(Estado estado);
    Boolean existsByEstadoAndCurso_IdCurso(Estado estado, Long id);

    @Query("""
    SELECT COUNT(g) > 0
    FROM Grupo g
    WHERE g.usuario.idUsuario = :idUsuario
      AND g.usuario.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.DOCENTE
      AND g.estado = com.app.pga.App.Models.Enum.Estado.HABILITADO
""")
    Boolean docenteGruposActivos (Long idUsuario);
}

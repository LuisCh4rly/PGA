package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Inscripcion;
import com.app.pga.App.Models.Enum.Estado;
import com.app.pga.App.Models.Enum.tipoInscripcion;
import com.app.pga.App.Models.Mappers.CursoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;
//jpaSpecification Executor para los filtros dinamicos
public interface IInscripcionRepository extends JpaRepository<Inscripcion, Long>, JpaSpecificationExecutor<Inscripcion> {
    Optional<Inscripcion> findByUsuario_IdUsuarioAndEstadoTrue(Long Idusuario);

    @Query("""
    SELECT i
    FROM Inscripcion i
    JOIN FETCH i.usuario
    WHERE i.estado = true
 """)
    List<Inscripcion>findByEstadoTrue();

    boolean existsByUsuario_IdUsuarioAndTipo(Long idUsuario, tipoInscripcion tipo);

    boolean existsByUsuario_IdUsuarioAndGrupo_IdGrupoAndGrupo_Estado(
            Long idUsuario,
            Long idGrupo,
            Estado estado
    );

    List<Inscripcion> findByGrupo_IdGrupoAndEstadoTrue(Long idGrupo);

    Optional<Inscripcion>findByIdInscripcionAndGrupo_IdGrupoAndEstadoTrue(Long idInscripcion, Long idGrupo);

   Boolean existsByGrupo_IdGrupoAndEstadoTrue(Long idGrupo);

    Page <Inscripcion> findAll(Specification<Inscripcion> filtrar, Pageable pageable);

    @Query("""
    SELECT COUNT(i) > 0
    FROM Inscripcion i
    WHERE i.usuario.idUsuario = :idUsuario
      AND i.usuario.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.ALUMNO
      AND i.estado = true
""")
    Boolean alumnosInscripcionesActivas (Long idUsuario);


    @Query("""
    SELECT i
    FROM Inscripcion i
    LEFT JOIN FETCH i.grupo g
    LEFT JOIN FETCH g.curso C
    WHERE i.usuario.idUsuario = :idAlumno
    ORDER BY i.fechaInscripcion DESC, i.estado DESC
    """)
    List<Inscripcion> obtenerInscripcionesAlumno(Long idAlumno);


}
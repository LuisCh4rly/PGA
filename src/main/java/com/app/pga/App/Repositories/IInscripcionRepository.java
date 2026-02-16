package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Inscripcion;
import com.app.pga.App.Models.Enum.Estado;
import com.app.pga.App.Models.Enum.tipoInscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IInscripcionRepository extends JpaRepository<Inscripcion, Long> {
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
}
package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Grupo;
import com.app.pga.App.Models.Enum.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IGrupoRepository extends JpaRepository<Grupo, Long> {
    boolean existsByNombreEqualsIgnoreCase(String nombre);
    List<Grupo> findByCursoIdCurso(Long idCurso);

    @Query("""
    SELECT g FROM Grupo g
    WHERE g.usuario.idUsuario = :idUsuario
    AND g.usuario.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.DOCENTE
""")
    List<Grupo> findGruposByDocente(@Param("idUsuario") Long idUsuario);

    Optional<Grupo> findByIdGrupo(Long idGrupo);

    List<Grupo> findByEstado(Estado estado);
}

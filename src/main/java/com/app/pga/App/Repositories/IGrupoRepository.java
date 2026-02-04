package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Grupo;
import com.app.pga.App.Models.Enum.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IGrupoRepository extends JpaRepository<Grupo, Long> {
    boolean existsByNombreEqualsIgnoreCase(String nombre);
    List<Grupo> findByCursoIdCurso(Long idCurso);
    List<Grupo> findByDocenteIdDocente(Long idDocente);
    Optional<Grupo> findByIdGrupo(Long idGrupo);

    List<Grupo> findByEstado(Estado estado);
}

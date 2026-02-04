package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ISesionRepository extends JpaRepository<Sesion, Long> {

    Optional<Sesion> findSesionByFecha(LocalDateTime fecha);
    Boolean existsByGrupo_Docente_IdDocenteAndFecha(Long idDocente, LocalDateTime fecha);
    List<Sesion> findByGrupo_IdGrupo(Long idGrupo);
    List<Sesion>findByGrupo_Docente_IdDocente(Long idDocente);
}
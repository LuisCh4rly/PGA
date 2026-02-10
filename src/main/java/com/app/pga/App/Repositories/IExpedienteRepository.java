package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Expediente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IExpedienteRepository extends JpaRepository<Expediente, Long> {
    Optional<Expediente> findByAlumno_IdAlumno(Long idAlumno);
    List<Expediente> findByAlumno_ActivoTrue();
}
package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Docente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IDocenteRepository extends JpaRepository<Docente, Long> {
    Optional<Docente> findByUsuario_IdUser(Long id);
    List <Docente>findByActivoTrue();
}
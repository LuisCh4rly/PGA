package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IAlumnoRepository extends JpaRepository<Alumno, Long> {
    Optional<Alumno> findByUsuario_IdUsuario(Long id);

    @Query("""
    SELECT a
    FROM Alumno a
    JOIN FETCH a.usuario
    WHERE a.activo = true
""")
    List<Alumno>findByActivoTrue();

    @Query("""
    SELECT a
    FROM Alumno a
    JOIN FETCH a.usuario
""")
    List<Alumno>findAll();
}

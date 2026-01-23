package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IDocenteRepository extends JpaRepository<Docente, Long> {
    Optional<Docente> findByUsuario_IdUser(Long id);
    @Query("""
    SELECT d
    FROM Docente d
    JOIN FETCH d.usuario
    WHERE d.activo = true
""")

    List <Docente>findByActivoTrue();
    @Query("""
    SELECT d
    FROM Docente d
    JOIN FETCH d.usuario
""")
    List<Docente> findAll();
}
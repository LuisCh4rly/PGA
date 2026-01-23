package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICursoRepository extends JpaRepository<Curso, Long> {
   boolean existsByNombreEqualsIgnoreCase(String nombre);
    List<Curso> findByActivoTrue();
}
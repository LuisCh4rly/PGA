package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAsistenciaRepository extends JpaRepository<Asistencia, Long> {
}
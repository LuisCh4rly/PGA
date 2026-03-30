package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface IAsistenciaRepository extends JpaRepository<Asistencia, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Asistencia a WHERE a.sesionAlumno.sesion.idSesion = :idSesion")
    void deleteBySesionAlumnoSesionIdSesion(@Param("idSesion") Long idSesion);
}
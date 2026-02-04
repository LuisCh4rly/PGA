package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Sesion;
import com.app.pga.App.Models.Entities.SesionAlumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISesionAlumnoRepository extends JpaRepository<SesionAlumno, Long> {

    void deleteBySesionIdSesion(Long idSesion);

    boolean existsBySesionIdSesionAndInscripcionIdInscripcion(Long idSesion, Long idInscripcion);

    List<SesionAlumno>findBySesionIdSesion(Long idSesion);

    List<SesionAlumno>findByInscripcion_IdInscripcion(Long idInscripcion);

}
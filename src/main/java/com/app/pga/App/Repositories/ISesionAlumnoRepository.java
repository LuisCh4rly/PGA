package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Sesion;
import com.app.pga.App.Models.Entities.SesionAlumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ISesionAlumnoRepository extends JpaRepository<SesionAlumno, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM SesionAlumno sa WHERE sa.sesion.idSesion = :idSesion")
    void deleteBySesionIdSesion(@Param("idSesion") Long idSesion);

    boolean existsBySesionIdSesionAndInscripcionIdInscripcion(Long idSesion, Long idInscripcion);

    List<SesionAlumno>findBySesionIdSesion(Long idSesion);

    List<SesionAlumno>findByInscripcion_IdInscripcion(Long idInscripcion);

}
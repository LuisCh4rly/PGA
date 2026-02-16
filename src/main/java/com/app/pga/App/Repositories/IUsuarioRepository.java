package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreAndApellidoPaternoAndApellidoMaterno(String Nombre, String ApellidoPaterno, String ApellidoMaterno);
    List<Usuario> findByActivoTrue();

    //---Usuario EN PARTICULAR
    Optional<Usuario>findUsuarioByIdUsuario(Long id);

    //DOCENTES
    //----LISTA DE DOCENTES ACTIVOS
    @Query("""
    SELECT u FROM Usuario u
    WHERE u.activo = true
    AND u.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.DOCENTE
""")
    List<Usuario> findDocentesActivos();

    //---LISTA GENERAL DE DOCENTES
    @Query("""
    SELECT u FROM Usuario u
    WHERE u.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.DOCENTE
""")
    List<Usuario> findDocentes();

    //--busqueda particular de un docente
    @Query("""
    SELECT u FROM Usuario u
    WHERE u.idUsuario = :id
    AND u.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.DOCENTE
""")
    Optional<Usuario> findDocenteById(Long id);


    //ALUMNO
    //---lISTA DE ALUMNOS ACTIVOS
    @Query("""
    SELECT u FROM Usuario u
    WHERE u.activo = true
    AND u.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.ALUMNO
""")
    List<Usuario> findAlumnosActivos();

    //--lISTA GENERAL DE DOCENTES
    @Query("""
    SELECT u FROM Usuario u
    WHERE u.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.ALUMNO
""")
    List<Usuario> findAlumnos();

    //BUSQUEDA PARTICULAR DE UN ALUMNO
    @Query("""
    SELECT u FROM Usuario u
    WHERE u.idUsuario = :id
    AND u.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.ALUMNO
""")
    Optional<Usuario> findAlumnoById(Long id);


}

package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Usuario;
import com.app.pga.App.Models.Specification.UsuarioSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

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
    Page<Usuario> findDocentes( Specification<Usuario> filtrar, Pageable pageable);

    //--lISTA GENERAL DE Alumnos conteo
    // Contar Docentes Inactivos
    @Query("""
    SELECT COUNT(u) FROM Usuario u 
    WHERE u.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.DOCENTE
    AND u.activo = true
""")
    int countDocentesActivos();

    // Contar Docentes Inactivos
    @Query("""
    SELECT COUNT(u) FROM Usuario u 
    WHERE u.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.DOCENTE
    AND u.activo = false
""")
    int countDocentesInactivos();

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

    //--lISTA GENERAL DE Alumnos
    @Query("""
    SELECT u FROM Usuario u
    WHERE u.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.ALUMNO
""")
    Page<Usuario> findAlumnos(Specification<Usuario> filtrar, Pageable pageable);

    //--lISTA GENERAL DE Alumnos conteo
    // Contar Alumnos Inactivos
    @Query("""
    SELECT COUNT(u) FROM Usuario u 
    WHERE u.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.ALUMNO 
    AND u.activo = true
""")
    int countAlumnosActivos();

    // Contar Alumnos Inactivos
    @Query("""
    SELECT COUNT(u) FROM Usuario u 
    WHERE u.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.ALUMNO 
    AND u.activo = false
""")
    int countAlumnosInactivos();

    //BUSQUEDA PARTICULAR DE UN ALUMNO
    @Query("""
    SELECT u FROM Usuario u
    WHERE u.idUsuario = :id
    AND u.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.ALUMNO
""")
    Optional<Usuario> findAlumnoById(Long id);

    Page <Usuario> findAll ( Specification <Usuario> filtar , Pageable pageable);
}

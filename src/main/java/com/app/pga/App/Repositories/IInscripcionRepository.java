package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Dtos.InscripcionResumenDto;
import com.app.pga.App.Models.Entities.Inscripcion;
import com.app.pga.App.Models.Enum.tipoInscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IInscripcionRepository extends JpaRepository<Inscripcion, Long> {
    Optional<Inscripcion> findByAlumno_IdAlumnoAndEstadoTrue(Long alumnoIdAlumno);

    @Query("""
    SELECT i
    FROM Inscripcion i
    JOIN FETCH i.alumno
    WHERE i.estado = true
 """)
    List<Inscripcion>findByEstadoTrue();

    @Query("""
    select new com.app.pga.App.Models.Dtos.InscripcionResumenDto(
        i.idInscripcion,
        i.tipo,
        i.estado,
        u.nombre
    )
    from Inscripcion i
    join i.alumno a
    join a.usuario u
""")
    List<InscripcionResumenDto> findAllResumen();

    boolean existsByAlumno_IdAlumnoAndTipo(Long idAlumno, tipoInscripcion tipo);

    boolean existsByAlumno_IdAlumnoAndGrupo_IdGrupoAndGrupo_Estado_Habilitado(Long idAlumno, Long idGrupo);
}
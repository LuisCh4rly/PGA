package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Expediente;
import com.app.pga.Auth.Models.Enum.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IExpedienteRepository extends JpaRepository<Expediente, Long> {

    @Query("""
        SELECT e FROM Expediente e
        WHERE e.usuario.idUsuario = :idUsuario
        AND e.usuario.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.ALUMNO
    """)
    Optional<Expediente> findExpedienteAlumnoByUsuarioId(Long idUsuario);

    @Query("""
    SELECT e FROM Expediente e
    WHERE e.usuario.activo = true
    AND e.usuario.cuenta.role.name = com.app.pga.Auth.Models.Enum.ERole.ALUMNO
""")
    List<Expediente> findExpedientesAlumnoActivo();
}
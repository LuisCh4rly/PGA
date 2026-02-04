package com.app.pga.App.Models.Dtos;

import com.app.pga.App.Models.Enum.EstadoAsistencia;

import java.io.Serializable;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.SesionAlumno}
 */
public record SesionAlumnoDetalleDto(
        Long idSesionAlumno,
        Long idInscripcion,
        String nombreAlumno,
        String apellidoPaterno,
        String apellidoMaterno,
        EstadoAsistencia estadoAsistencia
) implements Serializable { }
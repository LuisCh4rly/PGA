package com.app.pga.App.Models.Dtos;

import com.app.pga.App.Models.Enum.EstadoTarea;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.ActividadAlumno}
 */
public record ActividadAlumnoDto(
        Long idActividadAlumno,
        EstadoTarea estadoTarea,
        String comentarios,
        Boolean excento,
        String motivoExencion,
        String urlEntrega,
        LocalDate fechaEntrega,
        String observaciones,
        Long avanceGlobal,

        Long idInscripcion,

        String titulo,
        String descripcion,
        String campoFormativo,
        String origen,
        String urlInstrucciones,
        String fechaAsignacion,
        Boolean reqEntrega
) implements Serializable {
}
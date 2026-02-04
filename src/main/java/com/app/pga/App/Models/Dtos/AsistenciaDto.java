package com.app.pga.App.Models.Dtos;

import com.app.pga.App.Models.Enum.EstadoAsistencia;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Asistencia}
 */

public record AsistenciaDto (
        @NotNull(message = "Campo obligatorio")
        Long  idSesionAlumno,
        @NotNull(message = "Campo obligatorio")
        EstadoAsistencia estado) implements Serializable { }
package com.app.pga.App.Models.Dtos.ResponseDto;

import com.app.pga.App.Models.Dtos.InscripcionDto;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.SesionAlumno}
 */
public record SesionAlumnoDto(
        Long idSesionAlumno,
        @NotNull
        InscripcionDto inscripcion,
        @NotNull(message = "Campo Obligatorio")
        SesionDto sesion)
        implements Serializable {
}
package com.app.pga.App.Models.Dtos;

import com.app.pga.App.Models.Enum.tipoInscripcion;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Inscripcion}
 */
public record InscripcionDto(
        Long idInscripcion,
        LocalDate fechaInscripcion,
        @NotNull(message = "Campo obligatorio")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaInicio,
        @NotNull(message = "Campo obligatorio")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaFin,
        LocalDate fechaBaja,
        Boolean estado,
        @NotNull(message = "Campo oblligatorio")
        tipoInscripcion tipo,
        @NotNull
        AlumnoDto alumno,

        GrupoDto grupoDto)
        implements Serializable {
}
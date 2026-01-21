package com.app.pga.App.Models.Dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Alumno}
 */
public record AlumnoDto(
        Long idAlumno,
        @Size(message = "Máximo 30 carácteres", max = 30)
        @NotBlank(message = "Campo obligatorio.")
        String universidad,
        @Size(message = "Máximo 30 carácteres.", max = 30)
        @NotBlank(message = "Campo obligatorio.")
        String carrera,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaTermino,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaAlta,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaBaja,
        Boolean activo,
        @NotNull
        UsuarioDto usuarioDto)
        implements Serializable {
}
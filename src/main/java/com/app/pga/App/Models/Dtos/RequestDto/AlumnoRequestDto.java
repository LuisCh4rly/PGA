package com.app.pga.App.Models.Dtos.RequestDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Alumno}
 */
public record AlumnoRequestDto(
        @NotBlank(message = "Campo obligatorio.")
        String universidad,
        @NotBlank(message = "Campo obligatorio.")
        String carrera,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaTermino,
        @NotBlank(message = "Campo obligatorio.")
        Long idUsuario

) implements Serializable {
}
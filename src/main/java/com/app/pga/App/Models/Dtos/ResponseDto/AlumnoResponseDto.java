package com.app.pga.App.Models.Dtos.ResponseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

public record AlumnoResponseDto(
        Long idAlumno,
        @Size(message = "Máximo 60 carácteres", max = 60)
        @NotBlank(message = "Campo obligatorio.")
        String universidad,
        @Size(message = "Máximo 60 carácteres.", max = 60)
        @NotBlank(message = "Campo obligatorio.")
        String carrera,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaTermino,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaAlta,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaBaja,
        Boolean activo,
        @NotBlank(message = "Campo obligatorio.")
        String nombre,
        @NotBlank(message = "Campo obligatorio.")
        String apellidoPaterno,
        @NotBlank(message = "Campo obligatorio.")
        String apellidoMaterno
)  implements Serializable {

}
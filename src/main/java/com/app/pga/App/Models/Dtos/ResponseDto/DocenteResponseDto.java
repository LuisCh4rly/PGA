package com.app.pga.App.Models.Dtos.ResponseDto;

import com.app.pga.App.Models.Entities.Docente;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Docente}
 */
public record DocenteResponseDto(
        Long idDocente,
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
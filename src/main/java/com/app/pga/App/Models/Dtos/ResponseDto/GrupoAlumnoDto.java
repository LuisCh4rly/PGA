package com.app.pga.App.Models.Dtos.ResponseDto;

import com.app.pga.App.Models.Enum.Estado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record GrupoAlumnoDto(
        Long idGrupo,
        @Size(message = "Máximo 60 carácteres", max = 60)
        @NotBlank(message = "Campo obligatorio")
        String nombre,
        @NotNull
        Estado estado,
        String periodo,
        String nombreCurso,
        String docente,
        Long idInscripcion
) {
}

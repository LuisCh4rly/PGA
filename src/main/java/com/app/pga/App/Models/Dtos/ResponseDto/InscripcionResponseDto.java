package com.app.pga.App.Models.Dtos.ResponseDto;

import com.app.pga.App.Models.Enum.tipoInscripcion;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

public record InscripcionResponseDto(

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
        @Size(message = "Máximo 120 carácteres", max = 60)
        @NotBlank(message = "Campo obligatorio.")
        String escuela,
        @Size(message = "Máximo 120 carácteres.", max = 60)
        @NotBlank(message = "Campo obligatorio.")
        String nivelEstudio,
        @Size(message = "Máximo 120 carácteres.", max = 60)
        @NotBlank(message = "Campo obligatorio.")
        String carrera,
        @NotNull(message = "Campo oblligatorio")
        tipoInscripcion tipo,
        @NotNull
        String alumno,
        Long idGrupo,
        String nombreGrupo
) implements Serializable {

}

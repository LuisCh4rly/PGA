package com.app.pga.App.Models.Dtos.ResponseDto;

import com.app.pga.App.Models.Enum.EstadoTarea;

import java.io.Serializable;
import java.time.LocalDate;

public record ActividadALumnoListaDto(
        Long idActividadAlumno,

        Long idInscripcion,

        String titulo,
        String campoFormativo
) implements Serializable {
}

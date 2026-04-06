package com.app.pga.App.Models.Dtos.ResponseDto;

import com.app.pga.App.Models.Enum.EstadoTarea;

import java.time.LocalDate;

public record ActividadAlumnoResponseDTO(
        Long idActividadAlumno,
        EstadoTarea estadoTarea,
        String comentarios,
        Boolean excento,
        String motivoExencion,
        String urlEntrega,
        LocalDate fechaEntrega,
        String observaciones,
        Long idActividadGrupo,
        Long idInscripcion,
        String nombre
) {
}

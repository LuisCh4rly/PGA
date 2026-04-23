package com.app.pga.App.Models.Dtos.ResponseDto;

import com.app.pga.App.Models.Enum.Estado;

import java.io.Serializable;

public record InscripcionReporteDto(
        Long idInscripcion,
        Boolean estado,
        String alumno,
        String nombreGrupo,
        String nombreCurso,
        String Docente,
        Estado estadoGrupo
) implements Serializable {
}

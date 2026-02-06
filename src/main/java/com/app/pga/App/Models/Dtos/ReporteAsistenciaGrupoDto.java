package com.app.pga.App.Models.Dtos;

import com.app.pga.App.Models.Enum.Alcance;
import com.app.pga.App.Models.Enum.EstadoAsistencia;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReporteAsistenciaGrupoDto(
        Long idGrupo,
        String nombreGrupo,
        Long idSesion,
        LocalDateTime fechaSesion,
        String tema,
        Alcance alcance,
        Long idAlumno,
        String nombreAlumno,
        EstadoAsistencia estado
) {
}

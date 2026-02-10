package com.app.pga.App.Models.Dtos;

import com.app.pga.App.Models.Enum.Alcance;
import com.app.pga.App.Models.Enum.EstadoTarea;

import java.time.LocalDate;

public record ReporteSeguimientoDto (
        Long idGrupo,
        String nombreGrupo,
        Long idActividadGrupo,
        String titulo,
        Boolean reqEntrega,
        Alcance alcance,
        Long idActividadAlumno,
        EstadoTarea estadoTarea,
        Boolean excento,
        LocalDate fechaEntrega,
        Long idAlumno,
        String nombreAlumno
) {
}

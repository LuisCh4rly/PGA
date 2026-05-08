package com.app.pga.App.Models.Dtos;

import com.app.pga.App.Models.Enum.Alcance;
import com.app.pga.App.Models.Enum.EstadoTarea;
import com.app.pga.App.Models.Enum.Origen;

import java.time.LocalDate;

public record ReporteSeguimientoDto (
        Long idGrupo,
        String nombreGrupo,
        String periodo,
        com.app.pga.App.Models.Enum.Estado estadoGrupo,
        java.time.LocalDate createdAtGrupo,
        String nombreCurso,
        String nombreDocente,

        Long idActividadGrupo,
        String titulo,
        Boolean reqEntrega,
        Alcance alcance,
        Long idActividadAlumno,
        EstadoTarea estadoTarea,
        Boolean excento,
        LocalDate fechaEntrega,
        Long idAlumno,
        String nombreAlumno,
        Origen origen
) {
}

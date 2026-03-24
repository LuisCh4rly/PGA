package com.app.pga.App.Models.Dtos.ResponseDto;

import com.app.pga.App.Models.Enum.Estado;

public record GrupoResponseDashboardDto(
        Long idGrupo,
        String nombre,
        String nombreCurso,
        Long alumnos,
        Estado estado
) {

}

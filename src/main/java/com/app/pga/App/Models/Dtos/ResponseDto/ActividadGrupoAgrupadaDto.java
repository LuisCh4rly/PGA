package com.app.pga.App.Models.Dtos.ResponseDto;

import java.util.List;

public record ActividadGrupoAgrupadaDto(
        String titulo,
        String descripcion,
        String campo,
        List<GrupoResumenActividadDto>grupos
) {
}

package com.app.pga.App.Models.Dtos.RequestDto;

import java.util.List;

public record DetalleSeguimientoAgrupadoDto(
        Long  idSemana,
        List<DetalleSeguimientoRequestDto> actividades
) {
}

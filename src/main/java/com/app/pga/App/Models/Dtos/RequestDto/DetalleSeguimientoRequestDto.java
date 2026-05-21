package com.app.pga.App.Models.Dtos.RequestDto;

import com.app.pga.App.Models.Enum.EstadoTarea;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record DetalleSeguimientoRequestDto(
    Long idActividad,
    EstadoTarea estadoSemana,
    String observacionesAlumno,
    @Min(value = 0, message = "El valor mínimo es 0")
    @Max(value = 100, message = "El valor máximo es 100")
    Long avanceReal

) {
}

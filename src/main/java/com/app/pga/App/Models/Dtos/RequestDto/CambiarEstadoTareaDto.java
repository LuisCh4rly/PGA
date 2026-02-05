package com.app.pga.App.Models.Dtos.RequestDto;

import com.app.pga.App.Models.Enum.EstadoTarea;

public record CambiarEstadoTareaDto(

        EstadoTarea estado,
        String mensaje
) {
}

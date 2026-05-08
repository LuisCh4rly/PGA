package com.app.pga.App.Models.Dtos.RequestDto;

import com.app.pga.App.Models.Enum.Alcance;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AsignarActividadCatalogoMultipleDto(
        @NotNull(message = "Campo obligatorio")
        Long idActividadBase,

        @NotNull(message = "Campo obligatorio")
        Boolean reqEntrega,

        @NotNull(message = "Campo obligatorio")
        Alcance alcance,

        List<Long> idGrupos
) {
}

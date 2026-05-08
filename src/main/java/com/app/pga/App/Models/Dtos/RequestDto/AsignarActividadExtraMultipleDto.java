package com.app.pga.App.Models.Dtos.RequestDto;

import com.app.pga.App.Models.Enum.Alcance;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AsignarActividadExtraMultipleDto(
        @NotBlank(message = "Campo obligatorio")
        String titulo,
        @NotBlank(message = "Campo obligatorio")
        String descripcion,
        @NotNull(message = "Campo obligatorio")
        Boolean reqEntrega,
        @NotNull(message = "Campo obligatorio")
        Alcance alcance,
        List<Long> idGrupos
) {
}

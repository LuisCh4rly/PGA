package com.app.pga.App.Models.Dtos.RequestDto;

import com.app.pga.App.Models.Enum.Alcance;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.ActividadGrupo}
 */
public record AsignarActividadCatalogoDto(
        @NotNull (message = "Campo obligatorio")
        Long idActividadBase,

        @NotNull(message = "Campo obligatorio")
        Boolean reqEntrega,

        @NotNull(message = "Campo obligatorio")
        Alcance alcance,

        List<Long> idInscripciones
        ) implements Serializable {
}
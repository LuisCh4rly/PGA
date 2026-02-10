package com.app.pga.App.Models.Dtos.RequestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Documento}
 */
public record DocumentoRequestDto(
        @NotBlank(message = "Campo obligatorio")
        String tipo,
        @NotBlank(message = "Campo obligatorio")
        String nombre,
        @NotNull(message = "Campo obligatorio")
        Boolean obligatorio
) implements Serializable {
}
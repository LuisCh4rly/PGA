package com.app.pga.App.Models.Dtos.RequestDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Sesion}
 */
public record SesionUpdateDto(

        @NotNull(message = "Campo obligatorio")
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime fecha,
        @NotBlank(message = "Campo obligatorio")
        @Size(max = 120, message = "Máximo 120 caracteres")
        String tema,
        @NotBlank(message = "Campo obligatorio")
        @jakarta.validation.constraints.Pattern(regexp = "^(http|https)://.*$", message = "El link debe ser una URL válida")
        String urlSesion,
        @NotBlank(message = "Campo obligatorio")
        String plataforma
        ) implements Serializable {
}
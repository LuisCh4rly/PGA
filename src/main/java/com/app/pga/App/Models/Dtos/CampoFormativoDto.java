package com.app.pga.App.Models.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.CampoFormativo}
 */
public record CampoFormativoDto(
        Long idCampo,

        @Size(message = "Máximo 30 carácteres", max = 30)
        //@Pattern(message = "Sólo acepta letras y espacios", regexp = "^[\\\\p{L}A-Z]{2,30}$")
        @NotBlank(message = "Campo obligatorio")
        String nombre,

        @Size(message = "Máximo 60 carácteres", max = 60)
        //@Pattern(message = "Solo acepta letras y espacio", regexp = "^[\\\\p{L}\\\\s.,()-]+$")
        String descripcion,

        Boolean activo
    ) implements Serializable {
}